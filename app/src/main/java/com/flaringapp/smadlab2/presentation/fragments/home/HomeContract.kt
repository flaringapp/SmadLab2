package com.flaringapp.smadlab2.presentation.fragments.home

import com.flaringapp.smadlab2.presentation.mvp.IBaseFragment
import com.flaringapp.smadlab2.presentation.mvp.IBasePresenter
import io.reactivex.Observable

interface HomeContract {

    interface ViewContract: IBaseFragment {
        val numbersInputObservable: Observable<String>

        fun initInput(input: String)

        fun setNumbersError(error: Int?)

        fun setIntervals(intervals: List<IIntervalViewModel>)
    }

    interface PresenterContract: IBasePresenter<ViewContract> {
        fun onInput(input: String)
    }

    interface IIntervalViewModel {
        val interval: String
        val average: String
        val frequency: String
    }

}