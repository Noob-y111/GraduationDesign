package com.example.graduationdesign.base

import com.google.android.material.appbar.AppBarLayout
import kotlin.math.abs

abstract class AppBarStateChangedListener: AppBarLayout.OnOffsetChangedListener {

    enum class State {
        EXPANDED,   //展开
        COLLAPSED,   //折叠
        IDLE   //中间
    }

    private var mCurrentState = State.IDLE

    override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
        when {
            verticalOffset == 0 -> {
                if (mCurrentState != State.EXPANDED){
                    onStateChanged(appBarLayout, State.EXPANDED)
                }
                mCurrentState = State.EXPANDED
            }
            abs(verticalOffset) >= appBarLayout?.totalScrollRange!! -> {
                if (mCurrentState != State.COLLAPSED){
                    onStateChanged(appBarLayout, State.COLLAPSED)
                }
                mCurrentState = State.COLLAPSED
            }
            else -> {
                onStateChanged(appBarLayout, abs(verticalOffset))
                if (mCurrentState != State.IDLE){
                    onStateChanged(appBarLayout, State.IDLE)
                }
                mCurrentState = State.IDLE
            }
        }
    }

    abstract fun onStateChanged(appbar: AppBarLayout?, state: State)
    abstract fun onStateChanged(appbar: AppBarLayout?, verticalOffset: Int)
}