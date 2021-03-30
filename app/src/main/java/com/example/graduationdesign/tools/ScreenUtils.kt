package com.example.imitationqqmusic.model.tools

import android.app.Activity
import android.graphics.Point

class ScreenUtils {
    companion object{
        private fun getPoint(activity: Activity?): Point{
            val point = Point()
            activity?.window?.windowManager?.defaultDisplay?.getSize(point)
            return point
        }
        fun getWidth(activity: Activity?) = getPoint(activity).x
        fun getHeight(activity: Activity?) = getPoint(activity).y
    }
}