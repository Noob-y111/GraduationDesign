package com.example.graduationdesign.costume

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.widget.FrameLayout
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.ORIENTATION_HORIZONTAL
import kotlin.math.absoluteValue
import kotlin.math.sign

class ViewPager2Container @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var touchSlop = 0
    private var initialX = 0f
    private var initialY = 0f

    private val parentViewPager2: ViewPager2?
        get() {
            var v: View? = parent as? View
            while (v != null && v !is ViewPager2) {
                v = parent as View
            }
            return v as? ViewPager2
        }

    private val childViewPager2: ViewPager2?
        get() {
            return if (childCount > 0) getChildAt(0) as ViewPager2 else null
        }

    init {
        touchSlop = ViewConfiguration.get(context).scaledTouchSlop
    }

    private fun canChildScroll(orientation: Int, delta: Float): Boolean {
        val direction = -delta.sign.toInt()
        return when (orientation) {
            0 -> childViewPager2?.canScrollHorizontally(direction) ?: false
            1 -> childViewPager2?.canScrollVertically(direction) ?: false
            else -> throw IllegalArgumentException()
        }
    }

    private fun shouldInterceptTouchEvent(ev: MotionEvent?) {
        val orientation = parentViewPager2?.orientation ?: return

        if (!canChildScroll(orientation, -1f) && !canChildScroll(orientation, 1f)) {
            return
        }

        ev?.let { e ->
            if (e.action == MotionEvent.ACTION_DOWN) {
                initialX = e.x
                initialY = e.y
                parent.requestDisallowInterceptTouchEvent(true)
            } else if (e.action == MotionEvent.ACTION_MOVE) {
                val dx = e.x - initialX
                val dy = e.y - initialY

                val isViewPager2Horizontal = orientation == ORIENTATION_HORIZONTAL

                val scaledDx = dx.absoluteValue * if (isViewPager2Horizontal) .5f else 1f
                val scaledDy = dy.absoluteValue * if (isViewPager2Horizontal) 1f else .5f

                if (scaledDx > touchSlop || scaledDy > touchSlop) {
                    if (isViewPager2Horizontal == (scaledDy > scaledDx)) {
                        // Gesture is perpendicular, allow all parents to intercept
                        parent.requestDisallowInterceptTouchEvent(false)
                    } else {
                        // Gesture is parallel, query child if movement in that direction is possible
                        if (canChildScroll(orientation, if (isViewPager2Horizontal) dx else dy)) {
                            // Child can scroll, disallow all parents to intercept
                            parent.requestDisallowInterceptTouchEvent(true)
                        } else {
                            // Child cannot scroll, allow all parents to intercept
                            parent.requestDisallowInterceptTouchEvent(false)
                        }
                    }
                }
            }
        }
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        shouldInterceptTouchEvent(ev)
        return super.onInterceptTouchEvent(ev)
    }
}