package com.example.graduationdesign.view.songlist

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.graduationdesign.model.InternetModel
import com.example.graduationdesign.model.bean.song_list_bean.SongBean
import com.example.graduationdesign.model.bean.User

class SongListViewModel : ViewModel() {
    private lateinit var model: InternetModel

    private val _songList = MutableLiveData<ArrayList<SongBean>>()
    val songList: LiveData<ArrayList<SongBean>> = _songList

    private fun showList(list: ArrayList<SongBean>) {
        _songList.postValue(list)
    }

    fun initDataModel(context: Context) {
        model = InternetModel(context)
    }

    fun getRecommendDailySong(user: User) {
        user.cookie?.let {
            val map = HashMap<String, String>().apply {
                put("cookie", it)
                user.uid?.let { it1 -> put("uid", it1) }
            }
            model.getRecommendDailySong(map,
                {
                    this?.let { it1 -> showList(it1) }
                },
                {
                    println("================it: $it")
                })
        }
    }
}