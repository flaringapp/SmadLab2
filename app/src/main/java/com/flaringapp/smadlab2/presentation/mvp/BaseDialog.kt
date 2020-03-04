package com.flaringapp.smadlab2.presentation.mvp

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.annotation.CallSuper
import androidx.fragment.app.DialogFragment

abstract class BaseDialog<T : IBasePresenter<*>> : DialogFragment(), IBaseDialog {

    abstract val presenter: T

    override val viewContext: Context? get() = context

    override val dialogTag: String?
        get() = super.getTag()

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.onCreate(arguments, savedInstanceState)
    }

    override fun onResume() {
        super.onResume()

        dialog?.window?.apply {
            val params = attributes
            params.width = ViewGroup.LayoutParams.MATCH_PARENT
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT
            attributes = params
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            setCanceledOnTouchOutside(false)
            window?.requestFeature(Window.FEATURE_NO_TITLE)
            window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        }
    }

    @CallSuper
    override fun onSaveInstanceState(outState: Bundle) {
        presenter.saveInstanceState(outState)
        super.onSaveInstanceState(outState)
    }

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        onInitPresenter()
        super.onViewCreated(view, savedInstanceState)
        presenter.onStart()
    }

    @CallSuper
    override fun onDestroyView() {
        presenter.release()
        super.onDestroyView()
    }

    override fun close() {
        dismiss()
    }
}