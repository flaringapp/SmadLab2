package com.flaringapp.app.utils

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.util.TypedValue
import android.view.View
import android.view.View.*
import android.view.animation.AccelerateInterpolator
import android.view.animation.Interpolator
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import com.flaringapp.app.utils.ViewUtils.animateHide
import com.flaringapp.app.utils.ViewUtils.animateShow
import com.flaringapp.smadlab2.app.Constants.ANIM_DURATION


object ViewUtils {

    fun animateShow(view: View) {
        if (view.visibility == INVISIBLE || view.visibility == GONE) {
            view.visibility = VISIBLE
            view.alpha = 0f
        }

        animateAlpha(
            view,
            1f,
            object : AnimatorListenerAdapter() {
                override fun onAnimationStart(animation: Animator?) {
                    view.visibility = VISIBLE
                }
            }
        )
    }

    fun animateHide(view: View) {
        animateAlpha(
            view,
            0f,
            object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    view.visibility = GONE
                }
            },
            AccelerateInterpolator()
        )
    }

    fun animateAlpha(
        view: View,
        to: Float,
        listener: Animator.AnimatorListener? = null,
        interpolator: Interpolator? = null
    ) {
        val animator = ValueAnimator.ofFloat(view.alpha, to)
            .apply {
                duration = ANIM_DURATION
                this.interpolator = interpolator
                addListener(listener)
                addUpdateListener { view.alpha = it.animatedValue as Float }
                setTarget(view)
            }

        view.post {
            view.animate().cancel()
            animator.start()
        }
    }
}

fun View.gone(animate: Boolean = true) {
    if (animate) animateHide(this)
    else visibility = GONE
}

fun View.show(animate: Boolean = true) {
    if (animate) animateShow(this)
    else visibility = VISIBLE.also { alpha = 1f }
}

fun Context.dp(dp: Float): Float {
    return resources.dp(dp)
}

fun Context.dp(dp: Int): Float {
    return resources.dp(dp)
}

fun Resources.dp(dp: Float): Float {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, displayMetrics)
}

fun Resources.dp(dp: Int): Float {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), displayMetrics)
}

fun hideKeyboard(activity: Activity?) {
    if (activity == null) return
    val view = activity.currentFocus
    if (view != null) {
        val imm =
            activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}

fun Fragment.hideKeyboard() {
    view?.windowToken?.let {
        (context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)
            ?.hideSoftInputFromWindow(it, 0)
    }
}

fun Activity.showKeyboard() {
    if (currentFocus != null) {
        val imm =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(currentFocus, 0)
    }
}

fun Fragment.showKeyboard() {
    activity?.showKeyboard()
}

fun Context.toggleKeyboard() {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
}

fun View.setVisible(isVisible: Boolean, gone: Boolean = true) {
    if (isVisible) show()
    else hide(gone)
}

fun View.show() {
    visibility = VISIBLE
}

fun View.hide(gone: Boolean = false) {
    if (gone) gone()
    else visibility = INVISIBLE
}

fun View.gone() {
    visibility = GONE
}