package com.example.graduationdesign.tools

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast

class ToastUtil {
    companion object {
        private var toast: Toast? = null
        @SuppressLint("ShowToast")
        fun show(context: Context, str: String) {
            if (toast == null) {
                toast = Toast.makeText(context, str, Toast.LENGTH_SHORT)
            } else {
                toast?.setText(str)
            }
            toast?.show()
        }
    }
}