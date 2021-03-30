package com.example.graduationdesign.base

import android.app.Application
import cn.leancloud.AVOSCloud


class GraduationApplication : Application() {
    override fun onCreate() {
        super.onCreate()
//        AVOSCloud.initialize(
//            this,
//            "xXWIEsyqQpv2yaO6Y7Te7tfL-gzGzoHsz",
//            "qQAFuIhWDfTEkhcVNKASzXVc",
//            "https://xxwiesyq.lc-cn-n1-shared.com"
//        )
    }
}