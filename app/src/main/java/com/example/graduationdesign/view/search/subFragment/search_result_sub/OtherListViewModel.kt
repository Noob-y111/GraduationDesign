package com.example.graduationdesign.view.search.subFragment.search_result_sub

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.example.graduationdesign.model.InternetModel
import com.example.graduationdesign.model.SearchType
import com.example.graduationdesign.model.bean.search_bean.*
import com.example.graduationdesign.view.playlist.adapter.PlaylistAdapter
import com.google.gson.Gson

class OtherListViewModel: ViewModel() {

    private var model: InternetModel? = null

    private val _otherList = MutableLiveData<ArrayList<Any>>()
    val otherList: LiveData<ArrayList<Any>> = _otherList

    fun setInternetModel(model: InternetModel?){
        this.model = model
    }

    private fun changeOtherList(list: ArrayList<*>) {
        println("================list: $list")
        val newList = _otherList.value
        newList?.addAll(list)
        _otherList.postValue(newList)
    }

    fun clearList() {
        _otherList.value = ArrayList()
    }

    private fun pagingRequest(type: Int, keyword: String?) {
        getSearchResultByType(type, keyword)
    }

    fun pagingLoading(loadingEnable: Boolean, state: Int, type: Int, keyword: String?){
        if (loadingEnable){
            if (state == RecyclerView.SCROLL_STATE_IDLE){
                pagingRequest(type, keyword)
            }
        }
    }



    fun getSearchResultByType(type: Int, keyword: String?) {
        println("================type: $type")
        keyword?.let { notNullKeyWord ->
            model?.getOtherSearchResultByKeyword(HashMap<String, String>().also {
                it["keywords"] = notNullKeyWord
                it["type"] = type.toString()
                if(_otherList.value == null){
                    it["offset"] = "0"
                }else{
                    it["offset"] = _otherList.value!!.size.toString()
                }

            }, {
                println("================it $type json: $it")
                when (type) {
                    SearchType.SINGLE -> {
                        Gson().fromJson(it, ResultOfSingle::class.java).also { result ->
                            if (result.code == 200) {
                                changeOtherList(result.result.songs)
                            }
                        }
                    }
                    SearchType.ALBUM -> {
                        Gson().fromJson(it, ResultOfAlbum::class.java).also { result ->
                            if (result.code == 200) {
                                changeOtherList(result.result.albums)
                            }
                        }
                    }
                    SearchType.PLAYLIST -> {
                        Gson().fromJson(it, ResultOfPlaylist::class.java).also { result ->
                            if (result.code == 200) {
                                changeOtherList(result.result.playlists)
                            }
                        }
                    }
                    SearchType.ARTIST -> {
                        Gson().fromJson(it, ResultOfArtist::class.java).also { result ->
                            if (result.code == 200) {
                                changeOtherList(result.result.artists)
                            }
                        }
                    }
                    SearchType.VIDEO -> {
                        Gson().fromJson(it, ResultOfVideo::class.java).also { result ->
                            if (result.code == 200) {
                                changeOtherList(result.result.videos)
                            }
                        }
                    }
                }
            }, {

            })
        }

    }
}