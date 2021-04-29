package com.meloda.lineqrreader.widget

import android.content.Context
import android.text.InputType
import android.util.AttributeSet
import android.view.Gravity
import android.view.KeyEvent
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.widget.addTextChangedListener
import com.google.android.material.textfield.TextInputEditText
import com.meloda.lineqrreader.R
import com.meloda.lineqrreader.extensions.Extensions.setMaxLength
import com.meloda.lineqrreader.util.KeyboardUtils

class CodeEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayoutCompat(context, attrs, defStyleAttr) {

    private val editTexts: ArrayList<EditText> = arrayListOf()

    private var showKeyboardOnFocus = true

    private var currentPosition = 0

    private val editTextLayoutParams
        get() = LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

    var numberCount: Int = -1
        set(value) {
            field = value
            createEditTexts()
        }

    init {
        orientation = HORIZONTAL

        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        gravity = Gravity.CENTER

        val a = context.obtainStyledAttributes(attrs, R.styleable.CodeEditText)

        showKeyboardOnFocus = a.getBoolean(R.styleable.CodeEditText_showKeyboardOnFocus, true)

        var count = a.getInt(R.styleable.CodeEditText_numberCount, 4)
        if (count < 1) count = 4

        numberCount = count

        a.recycle()
    }

    private fun createEditTexts() {
        for (i in 0 until numberCount) {
            addView(createEditText())
        }

        editTexts[currentPosition].requestFocus()
    }

    private fun createEditText() =
        TextInputEditText(context, null, R.attr.editTextStyle).apply {
            editTexts.add(this)

            showSoftInputOnFocus = showKeyboardOnFocus
            layoutParams = editTextLayoutParams
            isCursorVisible = false
            gravity = Gravity.CENTER
            inputType = InputType.TYPE_CLASS_NUMBER

            setEms(2)
            setMaxLength(1)

            if (editTexts.size > 1) {
                (layoutParams as MarginLayoutParams).leftMargin = 20
            }

            setEditTextListeners(this)
        }

    private fun setEditTextListeners(editText: EditText) {
        with(editText) {
            this.setOnKeyListener { _, keyCode, event ->
                if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL) {
                    setText("")

                    if (currentPosition > 0) {
                        currentPosition--
                        editTexts[currentPosition].requestFocus()
                    } else {
                        KeyboardUtils.hideKeyboard(this)
                        clearFocus()
                    }
                }
                false
            }

            this.addTextChangedListener {
                if (text.toString().trim().isNotEmpty()) {
                    if (currentPosition == numberCount - 1) {
                        KeyboardUtils.hideKeyboard(this)
                        clearFocus()
                    } else {
                        currentPosition++
                        editTexts[currentPosition].requestFocus()
                    }
                }
            }

            this.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) setSelection(length())
            }
        }
    }

    override fun setOrientation(orientation: Int) {
        if (orientation != HORIZONTAL) throw IllegalStateException("setOrientation is not supported")
        super.setOrientation(orientation)
    }


    override fun isInEditMode(): Boolean {
        return true
    }
}