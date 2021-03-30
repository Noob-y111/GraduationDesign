package com.example.graduationdesign.tools

import com.android.volley.*


class JudgeVolleyError {
    companion object {
        fun judgeVolleyError(error: VolleyError?): String {
            if (error == null) return ""
            when (error) {
                is TimeoutError -> {
                    return "请求超时，请稍后重试"
                }

                is NoConnectionError -> {
                    return "网络错误,请检查网络"
                }

                is ServerError -> {
                    return "服务器异常"
                }

                is NetworkError -> {
                    return "服务器错误"
                }

                is ParseError -> {
                    return "数据格式错误"
                }

                else -> {
                    return "未知的错误"
                }
            }
        }
    }
}