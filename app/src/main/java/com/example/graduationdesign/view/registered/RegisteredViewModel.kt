package com.example.graduationdesign.view.registered

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.graduationdesign.R
import com.example.graduationdesign.model.InternetModel
import java.util.*

class RegisteredViewModel : ViewModel() {

    private var model: InternetModel? = null
    private var seconds = 10
//    private var isSent = true  //发送验证号码请求  3秒发一次

    private val _toastString = MutableLiveData<String>()
    val toastString: LiveData<String> = _toastString

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val _tvGetCodeEnable = MutableLiveData<Boolean>(false)
    val tvGetCodeEnable: LiveData<Boolean> = _tvGetCodeEnable

    private val _getCodeButtonText = MutableLiveData<String>("获取验证码")
    val getCodeButtonText: LiveData<String> = _getCodeButtonText

    private val _btnRegisteredEnabled = MutableLiveData(false)
    val btnRegisteredEnabled: LiveData<Boolean> = _btnRegisteredEnabled

    private val _registeredEnabled = MutableLiveData(false)

    fun setClickable(isClickable: Boolean) {
        _tvGetCodeEnable.postValue(isClickable)
    }

    fun changeBtnRegisteredEnabled(enable: Boolean){
        _btnRegisteredEnabled.postValue(enable)
    }

    private fun showMessage(str: String) {
        _toastString.value = str
    }

    private fun setError(str: String) {
        _errorMessage.postValue(str)
    }

    fun initInternetModel(context: Context) {
        model = InternetModel(context)
    }

    fun addUser(strUser: String, strCode: String) {
        if (_registeredEnabled.value == false) {
            showMessage("该账号已经注册！")
            return
        }
        model?.validationVerificationCode(strUser, strCode, _toastString){
            model?.registeredByCellphone(it, _toastString)
        }
    }

    fun getVerificationCode(number: String) {
        checkPhoneNumberIsExist(number){
            model?.getVerificationCode(number, _toastString)
        }
    }

    private fun checkPhoneNumberIsExist(number: String, block: ()->Unit) {
        model?.checkPhoneNumberIsExist(number, _errorMessage, _registeredEnabled, block)
    }

    private var timer: Timer? = null
    private var timerTask: TimerTask? = null

    fun btnGetCodeAppearance(){
        timer = Timer()
        timerTask = object : TimerTask(){
            override fun run() {
                seconds -= 1
                println("================seconds: $seconds")
                if (seconds >= 1){
                    _getCodeButtonText.postValue(seconds.toString())
                }else{
                    seconds = 10
                    setClickable(true)
                    _getCodeButtonText.postValue("获取验证码")
                    cancelTimer()
                }
            }
        }
        timer?.schedule(timerTask, 0, 1000)
    }

    private fun cancelTimer(){
        timerTask?.cancel()
        timer?.cancel()

        timer = null
        timerTask = null
    }

    override fun onCleared() {
        super.onCleared()
        println("=======================:onCleared")
        cancelTimer()
    }


    //不频繁设置 待解决
    fun isLengthOk(number: String, isEnable: Boolean) {
        _registeredEnabled.postValue(false)
        when {
            number.length < 11 -> {
                setError("手机号码不足11位")
                setClickable(false)
            }
            number.length == 11 -> {
//                checkPhoneNumberIsExist(number){
//
//                }
                setClickable(true)
            }
            else -> {
                setError("手机号码不能超过11位")
                setClickable(false)
            }
        }
    }
}