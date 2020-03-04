package com.flaringapp.smadlab2.presentation.dialogs.input

import com.flaringapp.smadlab2.presentation.dialogs.input.enums.DialogInputType
import com.flaringapp.smadlab2.presentation.mvp.IBaseDialog
import com.flaringapp.smadlab2.presentation.mvp.IBasePresenter
import io.reactivex.Observable

interface InputContract {
    interface ViewContract: IBaseDialog {
        val textChangeObservable: Observable<String>

        var headerVisible: Boolean
        var messageVisible: Boolean

        var positiveButtonEnabled: Boolean

        fun setHeader(header: String)
        fun setMessage(message: String)
        fun setInput(input: String)
        fun setHint(hint: String)

        fun setInputType(inputType: DialogInputType)

        fun setMaxLength(maxLength: Int)

        fun setInputError(errorRes: Int, vararg params: Any)
    }

    interface PresenterContract: IBasePresenter<ViewContract> {
        fun init(
            listener: InputDialogParent?,
            header: String?,
            message: String?,
            defaultInput: String?,
            hint: String?,
            inputType: DialogInputType,
            notEmpty: Boolean,
            minLength: Int,
            maxLength: Int
        )

        fun onOkClicked()
        fun onCancelClicked()
    }

    interface InputDialogParent {
        fun onInput(tag: String?, input: String)

        fun onInputCanceled(tag: String?) = Unit
    }
}
