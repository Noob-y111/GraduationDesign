package com.example.graduationdesign.costume

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatTextView

class TextViewWithDelete @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    private var block: (() -> Unit)? = null

    fun setCallbackOnClickDrawableEnd(block: () -> Unit) {
        this.block = block
    }

    override fun performClick(): Boolean {
        return super.performClick()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.let { e ->
            println("=======================Kongzhizhen:")
            val drawable = compoundDrawables[2]
            if (drawable == null) println("=======================drawable == null:")
            drawable?.let {
                if (e.action == MotionEvent.ACTION_UP && e.x > width - it.intrinsicWidth && e.x < width) {
                    println("=======================点击事件:")
                    block?.let { it1 ->
                        it1()
                    }
                }
            }
        }
        performClick()
        return super.onTouchEvent(event)
    }

}