package com.example.graduationdesign.tools

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesUtil {
    companion object{
        private var sharedPreferences: SharedPreferences? = null
        fun newInstance(context: Context): SharedPreferences = sharedPreferences ?: synchronized(this){
                context.getSharedPreferences("data", Context.MODE_PRIVATE).also {
                    sharedPreferences = it
                }
        }

        fun writeUidAndPassword(uid: String, password: String){
            sharedPreferences?.edit()?.let {
                it.putString("uid", uid)
                it.putString("password", password)
                it.apply()
            }
        }

        fun readUidAndPassword(block: (uid: String, password: String) -> Unit, notLogin: () -> Unit){
            sharedPreferences?.let {
                val uid = it.getString("uid", "")
                val password = it.getString("password", "")
                if (uid != "" && password != ""){
                    block(uid!!, password!!)
                }else{
                    notLogin()
                }
            }
        }

        fun removeUidAndPassword(){
            sharedPreferences?.edit()?.let {
                it.remove("uid")
                it.remove("password")
                it.apply()
            }
        }
    }
}