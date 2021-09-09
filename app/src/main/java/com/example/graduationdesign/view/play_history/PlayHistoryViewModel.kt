package com.example.graduationdesign.view.play_history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.graduationdesign.model.InternetModel
import com.example.graduationdesign.model.bean.song_list_bean.SongBean

class PlayHistoryViewModel : ViewModel() {

    private val _list = MutableLiveData<ArrayList<SongBean>>()
    val list: LiveData<ArrayList<SongBean>> = _list

    private var model: InternetModel? = null

    fun setModel(model: InternetModel?) {
        this.model = model
    }

    fun getPlayHisList() {
        model?.getPlayHistory {
            _list.postValue(it)
        }
    }

    fun contentBehavior(clickPosition: Int, indexOfData: Int){
        when(clickPosition){
            0 -> deleteSong(indexOfData)
            1 -> deleteAll()
        }
    }

    private fun deleteSong(position: Int) {
        model?.deletePlayHistory(list.value?.get(position)!!)
        getPlayHisList()
    }

    private fun deleteAll(){
        model?.deletePlayHistoryAll()
        getPlayHisList()
    }

}