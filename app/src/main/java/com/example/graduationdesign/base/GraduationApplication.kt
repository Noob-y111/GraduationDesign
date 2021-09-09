package com.example.graduationdesign.base

import android.app.Application
import cn.leancloud.AVOSCloud
import com.example.graduationdesign.model.AppDatabase
import com.example.graduationdesign.model.VolleySingleInstance
import com.example.graduationdesign.tools.SharedPreferencesUtil


class GraduationApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AppDatabase.newInstance(applicationContext)
        SharedPreferencesUtil.newInstance(applicationContext)

//        AVOSCloud.initialize(
//            this,
//            "xXWIEsyqQpv2yaO6Y7Te7tfL-gzGzoHsz",
//            "qQAFuIhWDfTEkhcVNKASzXVc",
//            "https://xxwiesyq.lc-cn-n1-shared.com"
//        )
    }
}