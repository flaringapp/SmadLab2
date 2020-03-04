package com.flaringapp.smadlab2.data.calculatior

import com.flaringapp.smadlab2.data.intervalSplitter.IntervalSplitter.IIntervalModel
import io.reactivex.Single

interface CharacteristicsCalculator {

    fun averageEmpirical(vararg intervals: IIntervalModel): Single<Double>

    fun mode(vararg intervals: IIntervalModel): Single<Double>

    fun median(vararg intervals: IIntervalModel): Single<Double>

    fun sampleSize(vararg intervals: IIntervalModel): Single<Double>

    fun variance(vararg intervals: IIntervalModel): Single<Double>

    fun meanSquareDeviation(vararg intervals: IIntervalModel): Single<Double>

    fun correctedVariance(vararg intervals: IIntervalModel): Single<Double>

    fun correctedMeanSquareDeviation(vararg intervals: IIntervalModel): Single<Double>

    fun variation(vararg intervals: IIntervalModel): Single<Double>

    fun asymmetry(vararg intervals: IIntervalModel): Single<Double>

    fun kurtosis(vararg intervals: IIntervalModel): Single<Double>

    fun startingPoint(power: Int, vararg intervals: IIntervalModel): Single<Double>

    fun centralPoint(power: Int, vararg intervals: IIntervalModel): Single<Double>
}