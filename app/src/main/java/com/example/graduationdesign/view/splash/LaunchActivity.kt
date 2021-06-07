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
import com.example.graduationdesign.model.InternetModel
import com.example.graduationdesign.model.bean.User
import com.example.graduationdesign.tools.SharedPreferencesUtil
import com.example.graduationdesign.tools.ToastUtil
import com.example.graduationdesign.view.bridge.BridgeActivity
import com.example.graduationdesign.view.login.LoginActivity
import com.example.graduationdesign.view.login.LoginActivityViewModel
import com.example.graduationdesign.view.main.MainActivity
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import kotlin.concurrent.thread

class LaunchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_launch)
        getPermissions()
    }

    private val handler = Handler()
    private val r = Runnable {
        startActivity(Intent(this, BridgeActivity::class.java))
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
            judgeIsLogin()
        }
    }

    private fun login(user: User){
        startActivity(Intent(this, MainActivity::class.java).also {
            Bundle().apply {
                putString("uid", user.uid)
                putString("head", user.avatarUrl)
                putString("background", user.backgroundUrl)
                putString("token", user.token)
                putString("name", user.nickName)
                putString("cookie", user.cookie)
                it.putExtra("user", this)
            }
        })
        finish()
    }

    private fun dealWithJson(json: String) {
        thread {
            JSONObject(json).also {
                when (it.getInt("code")) {
                    200 -> {
                        //todo 成功
                        val uid: String
                        val nickname: String
                        val avatarUrl: String
                        val backgroundUrl: String
                        val token = it.getString("token")
                        val cookie = it.getString("cookie")
                        it.getJSONObject("account").also { account ->
                            uid = account.getString("id")
                        }
                        it.getJSONObject("profile").also { profile ->
                            avatarUrl = profile.getString("avatarUrl")
                            backgroundUrl = profile.getString("backgroundUrl")
                            nickname = profile.getString("nickname")
                        }
                        val user = User(uid, nickname, avatarUrl, backgroundUrl, token, cookie)
                        MainScope().launch {
                            login(user)
                        }
                    }

                    502 -> {
                        //todo 密码错误
                    }
                }
            }
        }
    }

    private fun judgeIsLogin(){
        ToastUtil.show(applicationContext, "请稍候...")
        SharedPreferencesUtil.readUidAndPassword({ uid: String, password: String ->
            val model = InternetModel(this)
            if (uid.substring(uid.length - 8) == "@163.com"){
                val map = HashMap<String, String>()
                map["email"] = uid
                map["password"] = password
                model.loginByEmail(map, {
                    dealWithJson(it)
                }, {
                    handler.postDelayed(r, 2000)
                })
            }else{
                val map = HashMap<String, String>()
                map["phone"] = uid
                map["password"] = password
                model.loginByCellphone(map, {
                    dealWithJson(it)
                }, {
                    handler.postDelayed(r, 2000)
                })
            }
        }, {
            handler.postDelayed(r, 2000)
        })
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
                judgeIsLogin()
            } else {
                finish()
            }
        }
    }
}