package com.example.graduationdesign.callback

import com.android.volley.VolleyError

interface BaseBehaviorCallBack {
    fun dealWithJSON(json: String)
    fun errorBehavior(error: VolleyError)
}