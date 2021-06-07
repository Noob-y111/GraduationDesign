package com.example.graduationdesign.view.local

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.graduationdesign.model.InternetModel
import com.example.graduationdesign.model.bean.User
import com.example.graduationdesign.model.bean.playlist_bean.Playlist

class LocalViewModel : ViewModel() {

//    private var model: InternetModel? = null
//
//    fun setModel(model: InternetModel?) {
//        this.model = model
//    }
//
//    private val _playlist = MutableLiveData<ArrayList<Playlist>>()
//    val playlist: LiveData<ArrayList<Playlist>> = _playlist
//
//    fun getUserPlaylist(user: User) {
//        if (_playlist.value != null) return
//        model?.getUserPlaylistById(HashMap<String, String>().also {
//            it["uid"] = user.uid!!
//            it["cookie"] = user.cookie!!
//        }, {
//            _playlist.postValue(this)
//        }, {
//
//        })
//    }
}