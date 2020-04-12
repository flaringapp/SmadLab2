package com.flaringapp.smadlab2.presentation.fragments.home.impl

import androidx.core.text.isDigitsOnly
import com.flaringapp.smadlab2.data.intervalSplitter.IntervalSplitter
import com.flaringapp.smadlab2.data.intervalSplitter.IntervalSplitter.IIntervalModel
import com.flaringapp.smadlab2.presentation.fragments.home.HomeContract
import com.flaringapp.smadlab2.presentation.fragments.home.models.IntervalViewModel
import com.flaringapp.smadlab2.presentation.mvp.BasePresenter
import io.reactivex.disposables.Disposable
import java.text.DecimalFormat

class HomePresenter(
    private val intervalSplitter: IntervalSplitter
) : BasePresenter<HomeContract.ViewContract>(), HomeContract.PresenterContract {

    companion object {
        private const val SPACE = " "

        private val boundFormat: DecimalFormat = DecimalFormat("0.00")
        private val decimalFormat: DecimalFormat = DecimalFormat("0.0000")

        private const val DEFINED_INPUT = "0.65 0.79 0.57 0.34 0.25 0.00"
    }

    private var numbers: String = ""
    private var currentIntervals: List<IIntervalModel> = emptyList()

    private var numbersInputDisposable: Disposable? = null
    private var calculateIntervalsRequest: Disposable? = null
    private var calculationRequest: Disposable? = null

    private var pendingNumberInputAction: ((Int) -> Unit)? = null

    override fun onStart() {
        super.onStart()

        numbersInputDisposable = view!!.numbersInputObservable
            .doOnNext { input ->
                numbers = input

                if (validateNumbers()) {
                    updateCurrentIntervals()
                }
            }
            .subscribe {
                view?.setNumbersError(null)
            }

        view?.initInput(DEFINED_INPUT)
    }

    override fun release() {
        calculationRequest?.dispose()
        super.release()
    }

    override fun onInput(input: String) {
        if (!input.isDigitsOnly()) return
        pendingNumberInputAction?.invoke(input.toInt())
    }

    private fun updateCurrentIntervals() {
        val numbers = numbers.trim().split(SPACE)
            .map { it.trim() }
            .filter { it.isDecimal() && it != "." }
            .map { it.toDouble() }
            .toDoubleArray()

        calculateIntervalsRequest = intervalSplitter.splitToIntervals(*numbers)
            .doOnSuccess { currentIntervals = it }
            .subscribe(
                { view?.setIntervals(it.toViewModels()) },
                { view?.handleError(it) }
            )
    }

    private fun validateNumbers(): Boolean {
        if (numbers.trim().isEmpty()) return false

        val numbersSplit = numbers.trim().split(SPACE)
            .map { it.trim() }
            .filter { it.isNotEmpty() }
            .filter { it.isDecimal() }

        if (numbersSplit.isEmpty()) return false

        return true
    }

    private fun List<IIntervalModel>.toViewModels(): List<HomeContract.IIntervalViewModel> = map {
        IntervalViewModel(
            "[${boundFormat.format(it.leftBound)} ; ${boundFormat.format(it.rightBound)}]",
            decimalFormat.format(it.average),
            it.frequency.toString()
        )
    }

    private fun String.isDecimal(): Boolean {
        return let {
            if (startsWith('-')) {
                if (length == 1) return false
                substring(1)
            }
            else this
        }
            .find { !it.isDigit() && it != '.' } == null
    }
}
