package com.flaringapp.smadlab2.presentation.dialogs.input.impl

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import com.flaringapp.smadlab2.presentation.dialogs.input.InputContract
import com.flaringapp.smadlab2.presentation.dialogs.input.InputContract.InputDialogParent
import com.flaringapp.smadlab2.presentation.dialogs.input.enums.DialogInputType
import com.flaringapp.app.utils.hideKeyboard
import com.flaringapp.app.utils.setVisible
import com.flaringapp.smadlab2.R
import com.flaringapp.smadlab2.presentation.mvp.BaseDialog
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.dialog_input.*
import org.koin.android.ext.android.inject

class InputDialog : BaseDialog<InputContract.PresenterContract>(), InputContract.ViewContract {

    companion object {
        private const val HEADER_KEY = "key_header"
        private const val MESSAGE_KEY = "key_message"
        private const val INPUT_KEY = "key_input"
        private const val HINT_KEY = "key_hint"
        private const val INPUT_TYPE_KEY = "key_input_type"
        private const val NOT_EMPTY_KEY = "key_not_empty"
        private const val MIN_LENGTH_KEY = "key_min_length"
        private const val MAX_LENGTH_KEY = "key_max_length"

        private const val BUTTON_POSITIVE_ENABLED_ALPHA = 1f
        private const val BUTTON_POSITIVE_DISABLED_ALPHA = .25f

        @JvmStatic
        @JvmOverloads
        fun newInstance(
            header: String,
            message: String? = null,
            hint: String? = null,
            inputType: DialogInputType = DialogInputType.TEXT,
            notEmpty: Boolean = false,
            minLength: Int = 0,
            maxLength: Int = 0
        ): InputDialog {
            return InputDialog().apply {
                arguments = Bundle().apply {
                    putString(HEADER_KEY, header)
                    putString(MESSAGE_KEY, message)
                    putString(HINT_KEY, hint)
                    putSerializable(INPUT_TYPE_KEY, inputType)
                    putBoolean(NOT_EMPTY_KEY, notEmpty)
                    putInt(MIN_LENGTH_KEY, minLength)
                    putInt(MAX_LENGTH_KEY, maxLength)
                }
            }
        }
    }

    override val presenter: InputContract.PresenterContract by inject()

    override fun onInitPresenter() {
        presenter.view = this
        presenter.init(
            parentFragment as? InputDialogParent ?: context as? InputDialogParent,
            arguments!!.getString(HEADER_KEY),
            arguments!!.getString(MESSAGE_KEY),
            arguments!!.getString(INPUT_KEY),
            arguments!!.getString(HINT_KEY),
            arguments!!.getSerializable(INPUT_TYPE_KEY) as DialogInputType,
            arguments!!.getBoolean(NOT_EMPTY_KEY),
            arguments!!.getInt(MIN_LENGTH_KEY),
            arguments!!.getInt(MAX_LENGTH_KEY)
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_input, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initViews()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initViews() {
        buttonPositive.setOnClickListener { presenter.onOkClicked() }
        buttonNegative.setOnClickListener { presenter.onCancelClicked() }

        textInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                presenter.onOkClicked()
                true
            } else false
        }

        textInput.addTextChangedListener(inputTextWatcher)

        textInput.post {
            textInput.requestFocus()
            (context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                .showSoftInput(textInput, 0)
        }
    }

    override fun onDestroyView() {
        textInput.removeTextChangedListener(inputTextWatcher)
        super.onDestroyView()
    }

    private val inputTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(s: Editable) {
            textChangeSubject.onNext(s.toString())
        }
    }

    override var headerVisible: Boolean = true
        set(isVisible) {
            field = isVisible
            textHeader.setVisible(isVisible)
        }

    private val textChangeSubject = PublishSubject.create<String>()
    override val textChangeObservable: Observable<String> = textChangeSubject

    override var messageVisible: Boolean = true
        set(isVisible) {
            field = isVisible
            textMessage.setVisible(isVisible)
        }

    override var positiveButtonEnabled: Boolean = true
        get() = buttonPositive.isEnabled
        set(enabled) {
            if (field == enabled) return
            field = enabled
            buttonPositive.isEnabled = enabled
            buttonPositive.animate()
                .setDuration(200)
                .setInterpolator(AccelerateDecelerateInterpolator())
                .alpha(
                    if (enabled) BUTTON_POSITIVE_ENABLED_ALPHA
                    else BUTTON_POSITIVE_DISABLED_ALPHA
                )
        }

    override fun setHeader(header: String) {
        textHeader.text = header
    }

    override fun setMessage(message: String) {
        textMessage.text = message
    }

    override fun setInput(input: String) {
        textInput.setText(input)
    }

    override fun setHint(hint: String) {
        layoutInput.hint = hint
    }

    override fun setInputType(inputType: DialogInputType) {
        textInput.inputType = when (inputType) {
            DialogInputType.TEXT -> InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
            DialogInputType.NUMBER -> InputType.TYPE_CLASS_NUMBER
            DialogInputType.NUMBER_DECIMAL -> InputType.TYPE_CLASS_NUMBER or
                    InputType.TYPE_NUMBER_FLAG_DECIMAL
        }
    }

    override fun setMaxLength(maxLength: Int) {
        val currentInputFilters = textInput.filters
        val newInputFilters = mutableListOf(*currentInputFilters)
        newInputFilters.add(InputFilter.LengthFilter(maxLength))
        textInput.filters = newInputFilters.toTypedArray()
    }

    override fun setInputError(errorRes: Int, vararg params: Any) {
        layoutInput.error = getString(errorRes, params)
    }

    override fun close() {
        hideKeyboard()
        super.close()
    }
}