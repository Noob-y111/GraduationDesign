package com.example.graduationdesign.base

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import java.lang.Exception

abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getContentView())
        setToolBar(getToolBarId(), setToolBarTitle())
        transparentActionBar()
        initView()
    }

    private fun transparentActionBar() {
        window?.decorView?.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        window?.statusBarColor = Color.TRANSPARENT
    }

    private fun setToolBar(id: Int, title: String) {
        try {
            setSupportActionBar(findViewById<Toolbar>(id).also {
                it.title = title
            })
        } catch (e: Exception) {
            Log.d("hello", "setToolBar: ${e.message}")
        }
    }

    protected fun changeTitle(title: String) {
        supportActionBar?.title = title
    }

    protected abstract fun getToolBarId(): Int
    protected abstract fun initView()
    protected abstract fun setToolBarTitle(): String
    protected abstract fun getContentView(): View
}