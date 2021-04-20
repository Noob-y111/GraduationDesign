package com.example.graduationdesign.view.local_music

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.graduationdesign.model.InternetModel
import com.example.graduationdesign.model.bean.song_list_bean.SongBean

class LocalMusicViewModel : ViewModel() {

    private var model: InternetModel? = null

    private val _localSongList = MutableLiveData<ArrayList<SongBean>>()
    val localSongList: LiveData<ArrayList<SongBean>> = _localSongList

    fun setModel(model: InternetModel?){
        this.model = model
    }

    private fun changeList(list: ArrayList<SongBean>){
        _localSongList.postValue(list)
    }

    fun getLocalList(){
        model?.getLocalMusic({
            changeList(this)
        }, {

        })
    }

}