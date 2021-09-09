package com.example.graduationdesign.base

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.example.graduationdesign.R
import com.example.imitationqqmusic.model.tools.ScreenUtils
import kotlinx.android.synthetic.main.activity_main.view.*

abstract class BaseFragment : Fragment() {

    protected var toolbar: Toolbar? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        toolbar = getToolbarView()
        setTitle(getTitle())
        transparentActionBar()

        toolbar?.let {
            it.setOnMenuItemClickListener { item ->
                println("=======================点击了:")
                when (item.itemId) {
                    0 -> {
                        toSetting()?.let { action ->
                            Navigation.findNavController(requireActivity(), R.id.main_container)
                                .navigate(
                                    action
                                )
                        }
                    }
                }
                return@setOnMenuItemClickListener true
            }
        }
    }

    private fun transparentActionBar() {
        toolbar?.let {
            it.fitsSystemWindows = true
            requireActivity().apply {
                (this as AppCompatActivity).setSupportActionBar(it)
            }
        }

        val window = requireActivity().window
        window?.decorView?.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        window?.statusBarColor = Color.TRANSPARENT
    }

    private fun setTitle(title: String) {
        toolbar?.title = title
    }

    protected fun searchBoxWidth(view: View, actionId: Int) {
        val width = ScreenUtils.getWidth(requireActivity()) / 5 * 2
        view.layoutParams.width = width
        view.setOnClickListener {
            it.findNavController().navigate(actionId)
        }
    }

    protected abstract fun toSetting(): Int?
    abstract fun getTitle(): String
    abstract fun getToolbarView(): Toolbar?

}