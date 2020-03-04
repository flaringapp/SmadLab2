package com.flaringapp.smadlab2.app.di

import com.flaringapp.smadlab2.data.calculatior.CharacteristicsCalculator
import com.flaringapp.smadlab2.data.calculatior.CharacteristicsCalculatorImpl
import com.flaringapp.smadlab2.data.intervalSplitter.IntervalSplitter
import com.flaringapp.smadlab2.data.intervalSplitter.IntervalSplitterImpl
import org.koin.dsl.module

val dataModule = module {

    single { IntervalSplitterImpl() as IntervalSplitter }

    single { CharacteristicsCalculatorImpl() as CharacteristicsCalculator }

}