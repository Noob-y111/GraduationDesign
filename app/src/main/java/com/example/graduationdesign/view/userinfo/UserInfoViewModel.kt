package com.example.graduationdesign.view.userinfo

import android.graphics.Bitmap
import androidx.lifecycle.*
import com.example.graduationdesign.model.InternetModel
import com.example.graduationdesign.model.bean.User
import com.example.graduationdesign.model.bean.playlist_bean.Playlist
import com.example.graduationdesign.model.bean.user_info.UserDetailResponse
import java.io.File
import java.lang.Exception

class UserInfoViewModel : ViewModel() {

    companion object {
        private var viewModel: UserInfoViewModel? = null
        fun newInstance(lifecycleOwner: ViewModelStoreOwner): UserInfoViewModel =
            synchronized(this) {
                return@synchronized if (viewModel != null) {
                    viewModel!!
                } else {
                    viewModel =
                        ViewModelProvider(lifecycleOwner).get(UserInfoViewModel::class.java)
                    viewModel!!
                }
            }
    }

    private var model: InternetModel? = null

    private val _background = MutableLiveData<Any>()
    val background: LiveData<Any> = _background

    private val _head = MutableLiveData<Any>()
    val head: LiveData<Any> = _head

    private val _name = MutableLiveData<String>()
    val name: LiveData<String> = _name

    private val _backgroundAlpha = MutableLiveData<Float>()
    val backgroundAlpha: LiveData<Float> = _backgroundAlpha

    private val _playlist = MutableLiveData<ArrayList<Playlist>>()
    val playlist: LiveData<ArrayList<Playlist>> = _playlist

    private val _signature = MutableLiveData<String>("没有签名哦~")
    val signature: LiveData<String> = _signature

    private val _info = MutableLiveData<UserDetailResponse>()
    val info: LiveData<UserDetailResponse> = _info

    private val _string = MutableLiveData<String>()
    val string: LiveData<String> = _string

    fun setModel(model: InternetModel?) {
        this.model = model
    }

    fun changeAlpha(alpha: Float) {
        if (alpha < 0.4) {
            _backgroundAlpha.postValue(0.4f)
        } else {
            _backgroundAlpha.postValue(alpha)
        }
    }

    private fun changeInformation(detailResponse: UserDetailResponse) {
        _info.postValue(detailResponse)
        _background.postValue(detailResponse.profile.backgroundUrl)
        _head.postValue(detailResponse.profile.avatarUrl)
        _name.postValue(detailResponse.profile.nickname)
        detailResponse.profile.signature?.let {
            if (it.isNotEmpty())
                _signature.postValue(it)
        }
    }

    fun getUserDetail(user: User) {
        if (_head.value != null && _background.value != null && _name.value != null) return
        model?.getUserDetail(HashMap<String, String>().also {
            it["uid"] = user.uid!!
            it["cookie"] = user.cookie!!
        }, {
            changeInformation(it)
        }, {

        })
    }

    fun updateAvatar(user: User?, bitmap: Bitmap?) {
        user?.let {
            bitmap?.let {
                model?.updateAvatar(user, it)
            }
        }
    }

    private fun getMap(user: User?): HashMap<String, String> {
        _info.value!!.let { userDetail ->
            return HashMap<String, String>().also {
                userDetail.profile.apply {
                    it["birthday"] = birthday
                    it["province"] = province
                    it["city"] = city
                    it["cookie"] = user!!.cookie!!
                }
            }
        }
    }

    fun showSuccess(){
        _string.value = "修改成功"
    }

    private fun showMessage(string: String){
        _string.value = string
    }

    fun updateUserName(nickname: String, value: User?, block: () -> Unit) {
        model?.updateUser(getMap(value).also {
            it["nickname"] = nickname
            it["signature"] = _info.value!!.profile.signature!!
            it["gender"] = _info.value!!.profile.gender.toString()
        }, block, {
            it?.let { it1 -> showMessage(it1) }
        })
    }

    fun updateUserSignature(signature: String, value: User?, block: () -> Unit) {
        model?.updateUser(getMap(value).also {
            it["signature"] = signature
            it["nickname"] = _info.value!!.profile.nickname
            it["gender"] = _info.value!!.profile.gender.toString()
        }, block, {
            it?.let { it1 -> showMessage(it1) }
        })
    }

    fun updateUserSex(sex: Int, value: User?, block: () -> Unit) {
        model?.updateUser(getMap(value).also {
            it["gender"] = sex.toString()
            it["nickname"] =  _info.value!!.profile.nickname
            it["signature"] = _info.value!!.profile.signature!!
        }, block, {
            it?.let { it1 -> showMessage(it1) }
        })
    }

    fun getUserPlaylist(user: User) {
        if (_playlist.value != null) return
        model?.getUserPlaylistById(HashMap<String, String>().also {
            it["uid"] = user.uid!!
            it["cookie"] = user.cookie!!
        }, {
            _playlist.postValue(this)
        }, {

        })
    }

    fun returnNewFile(filePath: String): File? {
        return try {
            val file =
                File(filePath + File.separator + "take_photo_${System.currentTimeMillis()}.jpg")
            if (file.createNewFile()) {
                file
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}