package com.example.graduationdesign.costume

import android.content.Context
import android.graphics.Rect
import android.inputmethodservice.InputMethodService
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.AppCompatAutoCompleteTextView
import androidx.core.content.ContextCompat
import com.example.graduationdesign.R

class AutoCompleteTextViewWithClear @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatAutoCompleteTextView(context, attrs, defStyleAttr) {

    private var iconDrawable = ContextCompat.getDrawable(context, R.drawable.input_layout_clear)

    override fun onTextChanged(
        text: CharSequence?,
        start: Int,
        lengthBefore: Int,
        lengthAfter: Int
    ) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter)
        hideOrShowDrawableEnd()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.let { e ->
            iconDrawable?.let {
                if (e.action == MotionEvent.ACTION_UP
                    && e.x > width - it.intrinsicWidth
                    && e.x < width
                ) {
                    text?.clear()
                }
            }
        }
        performClick()
        return super.onTouchEvent(event)
    }

    override fun performClick(): Boolean {
        return super.performClick()
    }

    override fun onFocusChanged(focused: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect)
        hideOrShowDrawableEnd()
    }

    private fun hideOrShowDrawableEnd() {
        val icon =
            if (isFocusable && text?.isNotEmpty() == true) iconDrawable
            else null
        setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, icon, null)
    }

}