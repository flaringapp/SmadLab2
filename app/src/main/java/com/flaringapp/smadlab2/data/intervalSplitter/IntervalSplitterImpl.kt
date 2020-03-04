package com.flaringapp.smadlab2.data.intervalSplitter

import com.flaringapp.app.utils.rxCalculation
import com.flaringapp.smadlab2.data.intervalSplitter.models.IntervalModel
import io.reactivex.Single
import kotlin.math.log10

class IntervalSplitterImpl : IntervalSplitter {

    override fun splitToIntervals(
        vararg numbers: Double
    ): Single<List<IntervalSplitter.IIntervalModel>> = rxCalculation {
        val intervalsCount = getIntervalsCount(numbers.size)

        val min = numbers.min()!!
        val max = numbers.max()!!

        val intervalSize = (max - min) / intervalsCount

        List(intervalsCount) {
                createInterval(
                    min + intervalSize * it,
                    min + intervalSize * (it + 1),
                    it == intervalsCount - 1,
                    *numbers
                )
        }
    }

    private fun getIntervalsCount(numbersCount: Int): Int =
        (1 + 3.322 * log10(numbersCount.toDouble())).toInt()

    private fun createInterval(
        leftBound: Double,
        rightBound: Double,
        inclusive: Boolean = false,
        vararg numbers: Double
    ) =
        IntervalModel(
            leftBound,
            rightBound,
            if (inclusive) numbers.filter { it in leftBound..rightBound }
            else numbers.filter { it >= leftBound && it < rightBound }
    )

}