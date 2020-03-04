package com.flaringapp.smadlab2.data.calculatior

import com.flaringapp.app.utils.rxCalculation
import com.flaringapp.smadlab2.data.intervalSplitter.IntervalSplitter.IIntervalModel
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import kotlin.math.pow
import kotlin.math.sqrt

class CharacteristicsCalculatorImpl : CharacteristicsCalculator {

    override fun averageEmpirical(vararg intervals: IIntervalModel) = rxCalculation {
        intervals.sumByDouble { it.average * it.frequency } / intervals.sumBy { it.frequency }
    }

    override fun mode(vararg intervals: IIntervalModel) = rxCalculation {
        val modalInterval = intervals.maxBy { it.frequency }!!
        val modalIndex = intervals.indexOf(modalInterval)

        val previousIntervalFrequency =
            if (modalIndex == 0) 0
            else intervals[modalIndex - 1].frequency

        val nextIntervalFrequency =
            if (modalIndex == intervals.size - 1) 0
            else intervals[modalIndex + 1].frequency


        modalInterval.leftBound +
                intervals.size * (modalInterval.frequency - previousIntervalFrequency) /
                (2 * modalInterval.frequency - nextIntervalFrequency - previousIntervalFrequency)
    }

    override fun median(vararg intervals: IIntervalModel) = rxCalculation {
        var frequencySum = 0f
        var medianInterval: IIntervalModel = intervals[0]

        for (interval in intervals) {
            frequencySum += interval.frequency
            if (frequencySum > intervals.size / 2) {
                medianInterval = interval
                break
            }
        }

        frequencySum -= medianInterval.frequency
        medianInterval.leftBound + intervals[0].size / medianInterval.frequency *
                (intervals[0].size / 2 - frequencySum)

    }

    override fun sampleSize(vararg intervals: IIntervalModel) = rxCalculation {
        intervals.maxBy { it.rightBound }!!.rightBound - intervals.minBy { it.leftBound }!!.leftBound
    }

    override fun variance(vararg intervals: IIntervalModel) = averageEmpirical(*intervals)
        .map { average ->
            intervals.map {
                (it.average * it.frequency - average).let { value ->
                    value * value
                }
            }.sum() / intervals.sumBy { it.frequency }
        }

    override fun meanSquareDeviation(vararg intervals: IIntervalModel) = variance(*intervals)
        .map { variance ->
            sqrt(variance)
        }

    override fun correctedVariance(vararg intervals: IIntervalModel) = variance(*intervals)
        .map { variance ->
            intervals.size * variance / (intervals.size - 1)
        }

    override fun correctedMeanSquareDeviation(vararg intervals: IIntervalModel) =
        correctedVariance(*intervals)
            .map { correctedVariance ->
                sqrt(correctedVariance)
            }

    override fun variation(vararg intervals: IIntervalModel) = Single.zip(
        correctedMeanSquareDeviation(*intervals),
        averageEmpirical(*intervals),
        BiFunction<Double, Double, Double> { meanSquareDeviation, average ->
            meanSquareDeviation / average
        }
    )

    override fun asymmetry(vararg intervals: IIntervalModel) = Single.zip(
        centralPoint(3, *intervals),
        meanSquareDeviation(*intervals),
        BiFunction<Double, Double, Double> { centralPoint, meanSquareDeviation ->
            centralPoint / meanSquareDeviation.pow(3)
        }
    )

    override fun kurtosis(vararg intervals: IIntervalModel) = Single.zip(
        centralPoint(4, *intervals),
        meanSquareDeviation(*intervals),
        BiFunction<Double, Double, Double> { centralPoint, meanSquareDeviation ->
            (centralPoint / meanSquareDeviation.pow(4)) - 3
        }
    )

    override fun startingPoint(power: Int, vararg intervals: IIntervalModel) = rxCalculation {
        var result = 0.0
        intervals.associate { it.average to it.frequency }.let {
            for (model in it) {
                result += model.key.pow(power) * model.value
            }
        }
        result / intervals[0].size
    }

    override fun centralPoint(power: Int, vararg intervals: IIntervalModel) =
        averageEmpirical(*intervals)
            .map { average ->
                var result = 0.0
                intervals.associate { it.average to it.frequency }.let {
                    for (model in it) {
                        result += (model.key - average).pow(power) * model.value
                    }
                }
                result / intervals[0].size
            }
}