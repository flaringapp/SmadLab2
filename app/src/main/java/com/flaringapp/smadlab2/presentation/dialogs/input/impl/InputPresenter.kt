package com.flaringapp.smadlab2.presentation.dialogs.input.impl

import com.flaringapp.smadlab2.presentation.dialogs.input.InputContract
import com.flaringapp.smadlab2.presentation.dialogs.input.InputContract.InputDialogParent
import com.flaringapp.smadlab2.presentation.dialogs.input.enums.DialogInputType
import com.flaringapp.smadlab2.R
import com.flaringapp.smadlab2.presentation.mvp.BasePresenter
import io.reactivex.disposables.Disposable

class InputPresenter : BasePresenter<InputContract.ViewContract>(),
    InputContract.PresenterContract {

    private var listener: InputDialogParent? = null

    private var header: String? = null
    private var message: String? = null
    private lateinit var input: String
    private var hint: String? = null

    private lateinit var inputType: DialogInputType

    private var notEmpty = false
    private var minLength = 0
    private var maxLength = 0

    private lateinit var currentInput: String

    private var inputChangeDisposable: Disposable? = null

    override fun init(
        listener: InputDialogParent?,
        header: String?,
        message: String?,
        defaultInput: String?,
        hint: String?,
        inputType: DialogInputType,
        notEmpty: Boolean,
        minLength: Int,
        maxLength: Int
    ) {
        this.listener = listener
        this.header = header
        this.message = message
        this.input = defaultInput ?: ""
        this.currentInput = defaultInput ?: ""
        this.hint = hint
        this.inputType = inputType
        this.notEmpty = notEmpty;
        this.minLength = minLength
        this.maxLength = maxLength
    }

    override fun onStart() {
        super.onStart()

        if (header != null) {
            view?.headerVisible = true
            view?.setHeader(header!!)
        } else {
            view?.headerVisible = false
        }

        if (message != null) {
            view?.messageVisible = true
            view?.setMessage(message!!)
        } else {
            view?.messageVisible = false
        }

        view?.setInput(input)

        if (hint != null) {
            view?.setHint(hint!!)
        }

        view?.setInputType(inputType)

        maxLength.takeIf { it > 0 }
            ?.let { view?.setMaxLength(it) }

        if (notEmpty && currentInput.isEmpty()) {
            view?.positiveButtonEnabled = false
        }

        inputChangeDisposable = view!!.textChangeObservable
            .doOnNext { this.currentInput = it  }
            .subscribe {
                if (notEmpty) {
                    view?.positiveButtonEnabled = it.isNotEmpty()
                }
            }
    }

    override fun release() {
        inputChangeDisposable?.dispose()
        super.release()
    }

    override fun onOkClicked() {
        if (!validate(currentInput)) return

        listener?.onInput(view?.dialogTag, currentInput)

        view?.close()
    }

    private fun validate(text: String): Boolean {
        var valid = true

        if (text.length < minLength) {
            view?.setInputError(R.string.input_too_short, minLength)
            valid = false
        }

        return valid
    }

    override fun onCancelClicked() {
        listener?.onInputCanceled(view?.dialogTag)
        view?.close()
    }
}