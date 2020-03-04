package com.flaringapp.smadlab2.presentation.fragments.home.impl

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.flaringapp.app.utils.observeOnUI
import com.flaringapp.app.utils.onApiThread
import com.flaringapp.smadlab2.R
import com.flaringapp.smadlab2.presentation.dialogs.input.InputContract
import com.flaringapp.smadlab2.presentation.dialogs.input.enums.DialogInputType
import com.flaringapp.smadlab2.presentation.dialogs.input.impl.InputDialog
import com.flaringapp.smadlab2.presentation.fragments.home.HomeContract
import com.flaringapp.smadlab2.presentation.fragments.home.impl.adapter.IntervalsAdapter
import com.flaringapp.smadlab2.presentation.mvp.BaseFragment
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_home.*
import org.koin.androidx.scope.currentScope

class HomeFragment : BaseFragment<HomeContract.PresenterContract>(), HomeContract.ViewContract,
    InputContract.InputDialogParent {

    companion object {
        private const val NUMBER_INPUT_DIALOG = "dialog_number_input"

        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }

    override val presenter: HomeContract.PresenterContract by currentScope.inject()

    override fun onInitPresenter() {
        presenter.view = this
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initViews()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initViews() {
        inputNumbers.doAfterTextChanged {
            numbersInputSubject.onNext(it.toString())
        }

        recyclerIntervals.layoutManager =
            LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        recyclerIntervals.adapter = IntervalsAdapter()

        buttonAverage.setOnClickListener { presenter.onAverageClicked() }
        buttonMode.setOnClickListener { presenter.onModeClicked() }
        buttonMedian.setOnClickListener { presenter.onMedianClicked() }
        buttonSampleSize.setOnClickListener { presenter.onSampleSizeClicked() }
        buttonVariance.setOnClickListener { presenter.onVarianceClicked() }
        buttonMeanSquareDeviation.setOnClickListener { presenter.onMeanSquareDeviationClicked() }
        buttonCorrectedVariance.setOnClickListener { presenter.onCorrectedVarianceClicked() }
        buttonCorrectedMeanSquareDeviation.setOnClickListener { presenter.onCorrectedMeanSquareDeviationClicked() }
        buttonVariation.setOnClickListener { presenter.onVariationClicked() }
        buttonAsymmetry.setOnClickListener { presenter.onAsymmetryClicked() }
        buttonKurtosis.setOnClickListener { presenter.onKurtosisClicked() }
        buttonStartingPoint.setOnClickListener { presenter.onStartingPointClicked() }
        buttonCentralPoint.setOnClickListener { presenter.onCentralPointClicked() }
    }

    private val numbersInputSubject = PublishSubject.create<String>()
    override val numbersInputObservable: Observable<String> = numbersInputSubject
        .onApiThread()
        .observeOnUI()

    override fun initInput(input: String) {
        inputNumbers.setText(input)
    }

    override fun setButtonsVisible(visible: Boolean, animate: Boolean) {
        val alpha = if (visible) 1f else 0f
        if (animate) {
            layoutButtons.animate()
                .setDuration(250)
                .setInterpolator(AccelerateDecelerateInterpolator())
                .alpha(alpha)
        } else {
            layoutButtons.alpha = if (visible) 1f else 0f
        }
    }

    override fun setNumbersError(error: Int?) {
        layoutNumbersInput.error = error?.let { getString(it) }
    }

    override fun setIntervals(intervals: List<HomeContract.IIntervalViewModel>) {
        (recyclerIntervals.adapter as IntervalsAdapter).setItems(intervals)
    }

    override fun openNumberInputDialog() {
        InputDialog.newInstance(
            header = getString(R.string.input_power),
            minLength = 1,
            maxLength = 1,
            inputType = DialogInputType.NUMBER,
            notEmpty = true
        ).show(childFragmentManager, NUMBER_INPUT_DIALOG)
    }

    override fun setResult(result: String) {
        if (textResult.text == result) return
        textResult.animateText(result)
    }

    override fun onInput(tag: String?, input: String) {
        when (tag) {
            NUMBER_INPUT_DIALOG -> presenter.onInput(input)
        }
    }
}