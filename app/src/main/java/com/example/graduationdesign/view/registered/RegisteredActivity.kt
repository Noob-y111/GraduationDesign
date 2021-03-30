package com.example.graduationdesign.view.registered

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.graduationdesign.R
import com.example.graduationdesign.base.BaseActivity
import com.example.graduationdesign.tools.ToastUtil
import com.example.graduationdesign.databinding.ActivityRegisteredBinding

class RegisteredActivity : BaseActivity() {

    private lateinit var binding: ActivityRegisteredBinding
    private lateinit var viewModel: RegisteredViewModel

    override fun getToolBarId() = R.id.registered_toolbar
    override fun initView() {

        viewModel = ViewModelProvider(this).get(RegisteredViewModel::class.java)
        viewModel.initInternetModel(this)

        binding.registeredToolbar.apply {
            setNavigationIcon(R.drawable.back)
            setNavigationOnClickListener {
                finish()
            }
        }


        //click event
        binding.btnRegistered.setOnClickListener {
            with(binding) {
                viewModel.addUser(
                    registeredUserPhoneNumber.text.toString().trim(),
                    registeredCode.text.toString().trim(),
                )
            }
        }

        binding.btnGetCode.setOnClickListener {
            //点击禁用 一分钟一次
            viewModel.setClickable(false)
            viewModel.btnGetCodeAppearance()
            println("=======================setOnClickListener:获取验证码")
            viewModel.getVerificationCode(binding.registeredUserPhoneNumber.text.toString().trim())
        }

        with(binding){
            registeredUserPhoneNumber.addTextChangedListener(textWatcher)
            registeredUserPhoneNumber.addTextChangedListener(cellPhoneNumberWatcher)
            registeredCode.addTextChangedListener(textWatcher)
        }

        viewModel.toastString.observe(this, Observer {
            ToastUtil.show(this, it)
//            if (it == "注册成功"){
//                setResult(0, Intent().apply {
//                    putExtra("user", binding.registeredUserPhoneNumber.text.toString().trim())
//                })
//                finish()
//            }
        })

        //LiveData Observe
        viewModel.tvGetCodeEnable.observe(this, {
            binding.btnGetCode.isEnabled = it
            println("================binding.btnGetCode.isEnabled: ${binding.btnGetCode.isEnabled}")
        })

        viewModel.errorMessage.observe(this, {
            binding.registeredUserPhoneNumber.error = it
            binding.registeredUserPhoneNumber.isFocusable = true
        })

        viewModel.getCodeButtonText.observe(this, {
            binding.btnGetCode.text = it
        })

        viewModel.btnRegisteredEnabled.observe(this, {
            binding.btnRegistered.isEnabled = it
        })
    }

    override fun setToolBarTitle() = "注册"
    override fun getContentView(): View {
        binding = ActivityRegisteredBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.keep_in, R.anim.detail_dialog_exit)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private val cellPhoneNumberWatcher = object : TextWatcher{
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            val number = binding.registeredUserPhoneNumber.text.toString().trim()
            viewModel.isLengthOk(number, binding.btnGetCode.isEnabled)
        }

        override fun afterTextChanged(s: Editable?) {}

    }

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            with(binding) {
                val enabled = registeredUserPhoneNumber.text.toString()
                    .isNotEmpty() and registeredCode.text.toString().isNotEmpty()
                if (viewModel.btnRegisteredEnabled.value != enabled){
                    viewModel.changeBtnRegisteredEnabled(enabled)
                }
            }
        }

        override fun afterTextChanged(s: Editable?) {}

    }
}