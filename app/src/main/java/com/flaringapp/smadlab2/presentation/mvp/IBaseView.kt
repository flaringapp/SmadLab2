package com.flaringapp.smadlab2.presentation.mvp

import android.content.Context
import androidx.annotation.StringRes
import es.dmoral.toasty.Toasty

interface IBaseView {
    val viewContext: Context?

    fun onInitPresenter()

    fun showWarningToast(@StringRes textRes: Int) {
        viewContext?.let { Toasty.warning(it, textRes).show() }
    }

    fun showWarningToast(text: String) {
        viewContext?.let { Toasty.warning(it, text).show() }
    }

    fun showErrorToast(@StringRes textRes: Int) {
        viewContext?.let { Toasty.error(it, textRes).show() }
    }

    fun showErrorToast(text: String) {
        viewContext?.let { Toasty.error(it, text).show() }
    }

    fun showInfoToast(@StringRes textRes: Int) {
        viewContext?.let { Toasty.info(it, textRes).show() }
    }

    fun showInfoToast(text: String) {
        viewContext?.let { Toasty.info(it, text).show() }
    }

    fun showSuccessToast(@StringRes textRes: Int) {
        viewContext?.let { Toasty.success(it, textRes).show() }
    }

    fun showSuccessToast(text: String) {
        viewContext?.let { Toasty.success(it, text).show() }
    }

    fun handleError(e: Throwable) {
        e.message?.let { showErrorToast(it) }
    }
}