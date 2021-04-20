package com.example.graduationdesign.view.splash

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import cn.leancloud.AVUser
import com.example.graduationdesign.R
import com.example.graduationdesign.view.login.LoginActivity
import com.example.graduationdesign.view.login.LoginActivityViewModel
import com.example.graduationdesign.view.main.MainActivity

class LaunchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_launch)
        getPermissions()
    }

    private val r = Runnable {
        startActivity(Intent(this, LoginActivity::class.java))
        overridePendingTransition(R.anim.detail_dialog_enter, R.anim.keep_out)
        finish()
    }

    private fun getPermissions() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M
            && ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                100
            )
        } else {
            Handler().postDelayed(r, 2000)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(
            requestCode,
            permissions,
            grantResults
        )
        if (requestCode == 100) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Handler().postDelayed(r, 2000)
            } else {
                finish()
            }
        }
    }
}