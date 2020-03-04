package com.flaringapp.smadlab2.data.intervalSplitter

import io.reactivex.Single

interface IntervalSplitter {

    fun splitToIntervals(vararg numbers: Double): Single<List<IIntervalModel>>

    interface IIntervalModel {
        val leftBound: Double
        val rightBound: Double
        val size: Double
        val frequency: Int
        val average: Double
    }

}