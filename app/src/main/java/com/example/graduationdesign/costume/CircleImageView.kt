package com.example.graduationdesign.costume

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.withTranslation
import com.example.graduationdesign.R
import kotlin.math.min

class CircleImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    private var mRadius = 0f
    private val mPaint = Paint()
    private val mMatrix = Matrix()
    private var mScaleX = 0f
    private var mScaleY = 0f
    private lateinit var mBitmap: Bitmap
    private lateinit var mBitmapShader: BitmapShader

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mRadius = (min(w, h) / 2).toFloat()
    }

    private fun setBitmapShader(bitmap: Bitmap): BitmapShader{
        mBitmapShader = BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        mMatrix.setScale(mScaleX, mScaleY)
        mBitmapShader.setLocalMatrix(mMatrix)

        return mBitmapShader
    }

    override fun onDraw(canvas: Canvas?) {
        if (drawable == null) {
            super.onDraw(canvas)
            return
        }
        mBitmap = drawable.toBitmap()
        val bitmapWidth = mBitmap.width
        val bitmapHeight = mBitmap.height
        mScaleX = (mRadius * 2.0f) / bitmapWidth
        mScaleY = (mRadius * 2.0f) / bitmapHeight
        mPaint.shader = setBitmapShader(mBitmap)
        canvas?.drawCircle(mRadius, mRadius, mRadius, mPaint)
    }
}