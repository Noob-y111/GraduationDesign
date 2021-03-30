package com.example.graduationdesign.view.login

import android.content.Context
import androidx.lifecycle.*
import com.example.graduationdesign.model.InternetModel
import com.example.graduationdesign.model.bean.User
import com.example.graduationdesign.tools.JudgeVolleyError
import org.json.JSONObject
import kotlin.concurrent.thread

class LoginActivityViewModel : ViewModel() {

    private var model: InternetModel? = null

    companion object {
        private var viewModel: LoginActivityViewModel? = null
        fun newInstance(lifecycleOwner: ViewModelStoreOwner): LoginActivityViewModel =
            synchronized(this) {
                return@synchronized if (viewModel != null) {
                    viewModel!!
                } else {
                    viewModel =
                        ViewModelProvider(lifecycleOwner).get(LoginActivityViewModel::class.java)
                    viewModel!!
                }
            }
    }

//    private val _dialogIsCancelable = MutableLiveData<Boolean>()
//    val dialogIsCancelable: LiveData<Boolean> = _dialogIsCancelable

    private val _toastString = MutableLiveData<String>()
    val toastString: LiveData<String> = _toastString

    private val _showDialogFragment = MutableLiveData<Boolean>()
    val showDialogFragment: LiveData<Boolean> = _showDialogFragment

    private val _loginUser = MutableLiveData<User>()
    val loginUser: LiveData<User> = _loginUser

    fun initModel(context: Context) {
        model = InternetModel(context)
    }

    fun showDialog(enabled: Boolean) {
        _showDialogFragment.postValue(enabled)
    }

    private fun userLoginToMain(user: User){
        _loginUser.postValue(user)
    }

//    fun changeDialogIsCancelable(cancelable: Boolean) {
//        _dialogIsCancelable.postValue(cancelable)
//    }

    fun showMessage(str: String) {
        _toastString.value = str
    }

    fun loginByCellphone(user: String, password: String) {
        println("=======================:开始登录")
        val map = HashMap<String, String>()
        map["phone"] = user
        map["password"] = password
        model?.loginByCellphone(map,
            {
//                changeDialogIsCancelable(true)
                dealWithJson(it)
                showDialog(false)
                //todo 登陆成功
            },
            {
                val errorStr = JudgeVolleyError.judgeVolleyError(it)
                showMessage(errorStr)
                showDialog(false)
            })
    }

    fun loginByEmail(email: String, password: String) {
        val map = HashMap<String, String>()
        map["email"] = "$email@163.com"
        map["password"] = password
        model?.loginByEmail(map, {
            dealWithJson(it)
            showDialog(false)
            //todo 登陆成功
        }, {
            showMessage(JudgeVolleyError.judgeVolleyError(it))
            showDialog(false)
        })
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
                        userLoginToMain(user)
                    }

                    502 -> {
                        //todo 密码错误
                        showMessage(it.getString("msg"))
                    }
                }
            }
        }
    }

//    fun login(user: String, pwd: String) {
//        AVUser.logIn(user, pwd).subscribe(object : Observer<AVUser> {
//            override fun onSubscribe(d: Disposable) {}
//
//            override fun onNext(t: AVUser) {
//                showMessage("登录成功")
//            }
//
//            override fun onError(e: Throwable) {
//                showMessage(e.message.toString())
//            }
//
//            override fun onComplete() {}
//        })
//    }

}