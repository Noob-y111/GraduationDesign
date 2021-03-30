package com.example.graduationdesign.base

import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.example.graduationdesign.R
import com.example.imitationqqmusic.model.tools.ScreenUtils

abstract class BaseDialogFragment: DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        dialog?.window?.setWindowAnimations(R.style.player_dialog_fragment)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog?.window?.setWindowAnimations(getAnimId())
    }

    override fun onStop() {
        super.onStop()
        dialog?.window?.setWindowAnimations(0)
    }

    override fun onResume() {
        super.onResume()
        Handler().apply {
            postDelayed({
                dialog?.window?.setWindowAnimations(getAnimId())
            }, 500)
        }
    }

    abstract fun getAnimId(): Int

    protected fun translateStatusBar(){
        val window = dialog?.window
        window?.let {
            val point = Point()
            it.windowManager?.defaultDisplay?.getSize(point)
            it.attributes.height = point.y
            it.attributes.width = point.x
            it.setGravity(Gravity.BOTTOM)
            it.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            it.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            it.statusBarColor = Color.TRANSPARENT
        }
    }

    protected fun dialogBaseSetting(){
        val window = dialog?.window
        window?.let {
            val width = ScreenUtils.getWidth(requireActivity())
            it.attributes.width = (width * 0.8).toInt()
            it.setGravity(Gravity.CENTER)
            it.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }
}