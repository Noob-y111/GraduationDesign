package com.example.graduationdesign.view.userinfo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.graduationdesign.model.InternetModel
import com.example.graduationdesign.model.bean.User
import com.example.graduationdesign.model.bean.playlist_bean.Playlist
import com.example.graduationdesign.model.bean.user_info.UserDetailResponse

class UserInfoViewModel : ViewModel() {

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
        _background.postValue(detailResponse.profile.backgroundUrl)
        _head.postValue(detailResponse.profile.avatarUrl)
        _name.postValue(detailResponse.profile.nickname)
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
}