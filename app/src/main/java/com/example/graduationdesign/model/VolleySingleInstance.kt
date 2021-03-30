package com.example.graduationdesign.model

import android.content.Context
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley

class VolleySingleInstance private constructor(context: Context){
    companion object {
        private var volley: VolleySingleInstance? = null

        fun getInstance(context: Context) = volley ?: synchronized(this) {
            VolleySingleInstance(context).also {
                volley = it
            }
        }
    }

    val requestQueue: RequestQueue by lazy {
        Volley.newRequestQueue(context.applicationContext)
    }
}