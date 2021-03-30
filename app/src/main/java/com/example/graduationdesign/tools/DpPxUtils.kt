package com.example.imitationqqmusic.model.tools

import android.content.Context

class DpPxUtils {
    companion object{
        fun dp2Px(context: Context, dp: Float): Int{
            val scale = context.resources.displayMetrics.density
            return (dp * scale + 0.5f).toInt()
        }

        fun px2Dp(context: Context, px: Float): Int{
            val scale = context.resources.displayMetrics.density
            return (px/scale + 0.5f).toInt()
        }

        fun sp2Px(context: Context, sp: Float): Int{
            val scale = context.resources.displayMetrics.scaledDensity
            return (sp * scale + 0.5f).toInt()
        }

        fun px2Sp(context: Context, px: Float): Int{
            val scale = context.resources.displayMetrics.scaledDensity
            return (px / scale + 0.5f).toInt()
        }
    }
}