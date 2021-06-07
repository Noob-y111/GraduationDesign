package com.example.graduationdesign.view.bridge

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.view.View
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.graduationdesign.R
import com.example.graduationdesign.base.BaseActivity
import com.example.graduationdesign.databinding.ActivityBridgeBinding
import com.example.graduationdesign.view.login.LoginActivity
import com.example.graduationdesign.view.registered.RegisteredActivity

const val FINISH_BRIDGE = "finish"

class BridgeActivity : BaseActivity() {

    private lateinit var binding: ActivityBridgeBinding
    private val connection = Connection()

    override fun getToolBarId() = R.id.bridge_toolbar

    override fun initView() {
        binding.bridgeToolbar.setNavigationOnClickListener {
            finish()
        }

        binding.tvLoginBehavior.setOnClickListener {
            goToActivity(LoginActivity::class.java)
        }

        binding.tvRegisteBehavior.setOnClickListener {
            goToActivity(RegisteredActivity::class.java)
        }

        LocalBroadcastManager.getInstance(this).registerReceiver(connection, IntentFilter().also {
            it.addAction(
                FINISH_BRIDGE
            )
        })
    }

    override fun setToolBarTitle() = ""

    override fun getContentView(): View {
        binding = ActivityBridgeBinding.inflate(layoutInflater)
        return binding.root
    }

    private fun goToActivity(clazz: Class<*>) {
        Intent(this, clazz).apply {
            startActivity(this)
            overridePendingTransition(R.anim.fragment_in, R.anim.fragment_out)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(connection)
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.detail_dialog_enter, R.anim.keep_out)
    }

    inner class Connection : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == FINISH_BRIDGE) {
                this@BridgeActivity.finish()
            }
        }
    }
}