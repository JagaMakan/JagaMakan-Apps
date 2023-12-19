package com.mazpiss.jagamakan.ui.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.mazpiss.jagamakan.R

class EmailText : AppCompatEditText, View.OnTouchListener {
    private var clearButtonImage: Drawable = ContextCompat.getDrawable(context, R.drawable.baseline_close_24) as Drawable

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        init()
    }

    constructor(context: Context, attributeSet: AttributeSet, styleAttr: Int) : super(context, attributeSet, styleAttr) {
        init()
    }

    private fun init() {
        setOnTouchListener(this)

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.toString().isNotEmpty()) showClearButton() else hideClearButton()
            }

            private val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

            override fun afterTextChanged(s: Editable) {
                val input = s.toString()
                error = when {
                    input.isEmpty() -> "Silahkan masukan email yang valid"
                    !input.matches(emailPattern.toRegex()) -> "Masukan email yang valid"
                    else -> null
                }
            }
        })
    }

    override fun onTouch(v: View?, event: MotionEvent): Boolean {
        if (event.x > width - paddingEnd - clearButtonImage.intrinsicWidth) {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    clearButtonImage = ContextCompat.getDrawable(context, R.drawable.baseline_close_24) as Drawable
                    showClearButton()
                    return true
                }
                MotionEvent.ACTION_UP -> {
                    clearButtonImage = ContextCompat.getDrawable(context, R.drawable.baseline_close_24) as Drawable
                    text?.clear()
                    hideClearButton()
                    return true
                }
                else -> return false
            }
        }
        return false
    }

    private fun showClearButton() {
        setButtonDrawables(endOfTheText = clearButtonImage)
    }

    private fun hideClearButton() {
        setButtonDrawables()
    }

    private fun setButtonDrawables(
        startOfTheText: Drawable? = null,
        topOfTheText: Drawable? = null,
        endOfTheText: Drawable? = null,
        bottomOfTheText: Drawable? = null
    ) {
        setCompoundDrawablesWithIntrinsicBounds(
            startOfTheText,
            topOfTheText,
            endOfTheText,
            bottomOfTheText
        )
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }
}
