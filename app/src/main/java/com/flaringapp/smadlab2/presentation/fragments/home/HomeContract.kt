package com.flaringapp.smadlab2.presentation.fragments.home

import com.flaringapp.smadlab2.presentation.mvp.IBaseFragment
import com.flaringapp.smadlab2.presentation.mvp.IBasePresenter
import io.reactivex.Observable

interface HomeContract {

    interface ViewContract: IBaseFragment {
        val numbersInputObservable: Observable<String>

        fun initInput(input: String)

        fun setButtonsVisible(visible: Boolean, animate: Boolean)

        fun setNumbersError(error: Int?)

        fun setIntervals(intervals: List<IIntervalViewModel>)

        fun openNumberInputDialog()

        fun setResult(result: String)
    }

    interface PresenterContract: IBasePresenter<ViewContract> {
        fun onAverageClicked()
        fun onModeClicked()
        fun onMedianClicked()
        fun onSampleSizeClicked()
        fun onVarianceClicked()
        fun onMeanSquareDeviationClicked()
        fun onCorrectedVarianceClicked()
        fun onCorrectedMeanSquareDeviationClicked()
        fun onVariationClicked()
        fun onAsymmetryClicked()
        fun onKurtosisClicked()
        fun onStartingPointClicked()
        fun onCentralPointClicked()

        fun onInput(input: String)
    }

    interface IIntervalViewModel {
        val interval: String
        val average: String
        val frequency: String
    }

}