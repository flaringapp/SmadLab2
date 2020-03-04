package com.flaringapp.smadlab2.data.intervalSplitter.models

import com.flaringapp.smadlab2.data.intervalSplitter.IntervalSplitter

class IntervalModel(
    override val leftBound: Double,
    override val rightBound: Double,
    numbers: List<Double>
) : IntervalSplitter.IIntervalModel {

    override val size: Double = rightBound - leftBound

    override val frequency: Int = numbers.size

    override val average: Double = (leftBound + rightBound) / 2
}