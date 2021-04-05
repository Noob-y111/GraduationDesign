package com.example.graduationdesign.view.playlist

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.graduationdesign.model.InternetModel
import com.example.graduationdesign.model.bean.playlist_bean.Playlist
import com.example.graduationdesign.model.bean.playlist_bean.Tag

class PlaylistViewModel : ViewModel() {

    private lateinit var model: InternetModel

    private val _tabItemNameList = MutableLiveData<ArrayList<Tag>>()
    val tabItemNameList: LiveData<ArrayList<Tag>> = _tabItemNameList

//    private val _playlists = MutableLiveData<ArrayList<ArrayList<Playlist>>>().also {
//        it.postValue(ArrayList<ArrayList<Playlist>>())
//    }
//    val playlists: LiveData<ArrayList<ArrayList<Playlist>>> = _playlists

    fun initInternetModel(context: Context) {
        model = InternetModel(context)
    }

    fun getInternetViewModel() = model

    private fun showTags(tags: ArrayList<Tag>) {
        _tabItemNameList.postValue(tags)
    }

    fun getTags() {
        model.getHotPlaylistTag({
            this?.let { showTags(it) }
        }, {
            println("================it: $it")
        })
    }
}
