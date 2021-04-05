package com.example.graduationdesign.view.login

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.graduationdesign.R
import com.example.graduationdesign.base.BaseActivity
import com.example.graduationdesign.databinding.ActivityLoginBinding
import com.example.graduationdesign.tools.ToastUtil
import com.example.graduationdesign.view.dialog.WaitingDialog
import com.example.graduationdesign.view.main.MainActivity
import com.example.graduationdesign.view.registered.RegisteredActivity
import com.example.imitationqqmusic.model.tools.DpPxUtils
import com.example.imitationqqmusic.model.tools.ScreenUtils
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class LoginActivity : BaseActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginActivityViewModel

    private val dialog = WaitingDialog().also { it.isCancelable = false }

    override fun getToolBarId() = R.id.login_toolbar
    override fun setToolBarTitle() = ""

    override fun getContentView(): View {
        binding = ActivityLoginBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initView() {
        viewModel = LoginActivityViewModel.newInstance(this)
        viewModel.initModel(this)
        initViewPager()

        binding.loginToolbar.setNavigationOnClickListener{
            finish()
        }

        //observe
        viewModel.toastString.observe(this, {
            ToastUtil.show(this, it)
        })

        viewModel.loginUser.observe(this, { user ->
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
        })

//        viewModel.dialogIsCancelable.observe(this, {
//            dialog.isCancelable = it
//        })

        viewModel.showDialogFragment.observe(this, {
            if (it) dialog.show(supportFragmentManager, null) else dialog.dismiss()
        })
    }

    private fun initViewPager() {
        binding.loginVp2.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount() = 2

            override fun createFragment(position: Int) = if (position == 1) {
                EmailFragment()
            } else {
                AccountFragment()
            }
        }

        binding.loginTabLayout.layoutParams.width =
            ScreenUtils.getWidth(this) - DpPxUtils.dp2Px(this, 40f)
        TabLayoutMediator(binding.loginTabLayout, binding.loginVp2) { tab: TabLayout.Tab, i: Int ->
            when (i) {
                1 -> tab.text = "网易邮箱"
                else -> tab.text = "账号密码"
            }
        }.attach()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menu?.add(Menu.NONE, 0, 0, "注册")?.setIcon(R.drawable.ic_register)
            ?.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == 0) {
            startActivity(Intent(this, RegisteredActivity::class.java))
            overridePendingTransition(R.anim.detail_dialog_enter, R.anim.keep_out)
        }
        return super.onOptionsItemSelected(item)
    }

//textview 部分点击事件
//    private fun changeTextColor(color: Int) {
//        val mSpannableStringBuilder = SpannableStringBuilder(binding.tvToRes.text)
//        val mForegroundColorSpan = ForegroundColorSpan(color)
//
//        mSpannableStringBuilder.setSpan(object : ClickableSpan() {
//            override fun onClick(widget: View) {
////                Toast.makeText(this@LoginActivity, "点击了", Toast.LENGTH_SHORT).show()
//                startActivityForResult(Intent(this@LoginActivity, RegisteredActivity::class.java), 0)
//                overridePendingTransition(R.anim.detail_dialog_enter, R.anim.keep_out)
//            }
//        }, 5, 9, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
//        binding.tvToRes.text = mSpannableStringBuilder
//
//        mSpannableStringBuilder.setSpan(
//            mForegroundColorSpan,
//            5,
//            9,
//            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
//        )
//        binding.tvToRes.movementMethod = LinkMovementMethod.getInstance()
//        binding.tvToRes.text = mSpannableStringBuilder
//        binding.tvToRes.highlightColor = Color.TRANSPARENT
//    }

}