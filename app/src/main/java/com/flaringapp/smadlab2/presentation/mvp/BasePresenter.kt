package com.flaringapp.smadlab2.presentation.mvp

import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.annotation.StringRes

abstract class BasePresenter<T : IBaseView>: IBasePresenter<T> {

    final override var view: T? = null

    override fun onCreate(arguments: Bundle?, savedInstanceState: Bundle?) {}
    override fun saveInstanceState(outState: Bundle) {}

    @CallSuper
    override fun onStart() {}

    @CallSuper
    override fun release() {
        view = null
    }

    protected fun getString(@StringRes res: Int): String? {
        return view?.viewContext?.getString(res)
    }
}