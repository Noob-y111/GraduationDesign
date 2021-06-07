package com.example.graduationdesign.costume

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import androidx.core.view.children
import androidx.viewpager2.widget.ViewPager2
import kotlin.math.abs

class ViewPager2Container @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private var startX = 0f
    private var startY = 0f
    private var childViewPager2: ViewPager2? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        for (i in children) {
            if (i is ViewPager2) {
                childViewPager2 = i
                break
            }
        }
        childViewPager2 ?: kotlin.run {
            throw Exception("子View没有ViewPager2")
        }
    }

    private fun shouldInterceptTouchEvent(e: MotionEvent?): Boolean {
        e?.let {
            childViewPager2?.let { vp2 ->
                if (!vp2.isUserInputEnabled || (vp2.adapter != null && vp2.adapter?.itemCount!! <= 1)) {
                    return super.onInterceptTouchEvent(e)
                }
                when (e.action) {
                    MotionEvent.ACTION_DOWN -> {
                        startX = e.x
                        startY = e.y
                        parent.requestDisallowInterceptTouchEvent(false)
                    }
                    MotionEvent.ACTION_MOVE -> {
                        val endX = e.x
                        val distanceX = abs(e.x - startX)
                        val distanceY = abs(e.y - startY)
                        if (childViewPager2!!.orientation == ViewPager2.ORIENTATION_HORIZONTAL) {
                            horizontalScroll(endX, distanceX, distanceY)
                        }
                    }
                }
            }
        }
        return super.onInterceptTouchEvent(e)
    }

    private fun horizontalScroll(endX: Float, disX: Float, disY: Float) {
        childViewPager2!!.adapter?.let {
            when{
                disX > disY -> { //横向滑动
                    when{
                        endX > startX -> { //右滑
                            if (childViewPager2!!.currentItem == 0){
                                parent.requestDisallowInterceptTouchEvent(false)
                            }else{
                                parent.requestDisallowInterceptTouchEvent(true)
                            }
                        }
                        endX < startX -> { //左滑
                            if (childViewPager2!!.currentItem == it.itemCount-1){
                                parent.requestDisallowInterceptTouchEvent(false)
                            }else{
                                parent.requestDisallowInterceptTouchEvent(true)
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return shouldInterceptTouchEvent(ev)
    }
}