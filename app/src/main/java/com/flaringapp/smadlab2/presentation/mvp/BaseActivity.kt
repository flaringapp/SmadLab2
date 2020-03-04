package com.flaringapp.smadlab2.presentation.mvp

import android.content.Context
import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity<T : IBasePresenter<*>> : AppCompatActivity(), IBaseView {

    abstract val presenter: T

    override val viewContext: Context? get() = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.onCreate(intent.extras, savedInstanceState)
    }

    @CallSuper
    override fun onSaveInstanceState(outState: Bundle) {
        presenter.saveInstanceState(outState)
        super.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        presenter.release()
        super.onDestroy()
    }
}