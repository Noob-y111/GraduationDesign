package com.example.graduationdesign.costume

import com.android.volley.Response
import com.android.volley.toolbox.StringRequest

class StringPostRequest(
    method: Int,
    url: String?,
    listener: Response.Listener<String>?,
    errorListener: Response.ErrorListener?
) : StringRequest(method, url, listener, errorListener) {

    private var map = HashMap<String, String>()

    fun putValues(key: String, value: String){
        map[key] = value
    }

    fun putValues(otherMap: HashMap<String, String>){
        map = otherMap
    }

    override fun getParams(): MutableMap<String, String> {
        return map
    }

}