package com.flaringapp.smadlab2.presentation.fragments.home.impl

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.flaringapp.app.utils.observeOnUI
import com.flaringapp.app.utils.onApiThread
import com.flaringapp.smadlab2.R
import com.flaringapp.smadlab2.presentation.fragments.home.HomeContract
import com.flaringapp.smadlab2.presentation.fragments.home.impl.adapter.IntervalsAdapter
import com.flaringapp.smadlab2.presentation.mvp.BaseFragment
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_home.*
import org.koin.androidx.scope.currentScope

class HomeFragment : BaseFragment<HomeContract.PresenterContract>(), HomeContract.ViewContract{

    companion object {

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
    }

    private val numbersInputSubject = PublishSubject.create<String>()
    override val numbersInputObservable: Observable<String> = numbersInputSubject
        .onApiThread()
        .observeOnUI()

    override fun initInput(input: String) {
        inputNumbers.setText(input)
    }

    override fun setNumbersError(error: Int?) {
        layoutNumbersInput.error = error?.let { getString(it) }
    }

    override fun setIntervals(intervals: List<HomeContract.IIntervalViewModel>) {
        (recyclerIntervals.adapter as IntervalsAdapter).setItems(intervals)
    }
}