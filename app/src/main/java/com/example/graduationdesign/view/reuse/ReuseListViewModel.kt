package com.example.graduationdesign.view.reuse

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.graduationdesign.model.InternetModel
import com.example.graduationdesign.model.ListType
import com.example.graduationdesign.model.bean.new_album_bean.SingleMonthData
import com.example.graduationdesign.model.bean.playlist_bean.Playlist
import com.example.graduationdesign.model.bean.ranking_list_bean.ListDetail
import com.example.graduationdesign.model.bean.song_list_bean.SongBean

class ReuseListViewModel : ViewModel() {

    private var model: InternetModel? = null

    private val _appbarImage = MutableLiveData<Any>()
    val appbarImage: LiveData<Any> = _appbarImage

    private val _toolbarTitle = MutableLiveData<String>()
    val toolbarTitle: LiveData<String> = _toolbarTitle

    private val _labelTitle = MutableLiveData<String>()
    val labelTitle: LiveData<String> = _labelTitle

    private val _creator = MutableLiveData<String>()
    val creator: LiveData<String> = _creator

    private val _description = MutableLiveData<String>()
    val description: LiveData<String> = _description

    private val _list = MutableLiveData<ArrayList<SongBean>>()
    val list: LiveData<ArrayList<SongBean>> = _list

    private val _shouldHideProgress = MutableLiveData<Boolean>()
    val shouldHideProgress: LiveData<Boolean> = _shouldHideProgress

    fun initInternetModel(context: Context) {
        this.model = InternetModel(context)
    }

    private fun refreshView(
        recyclerList: ArrayList<SongBean>,
        appbarImageUrl: Any?,
        creatorName: String,
        description: String,
        labelTitle: String,
        toolbarTitle: String,
        shouldHideProgress: Boolean
    ) {
        _appbarImage.postValue(appbarImageUrl)
        _creator.postValue(creatorName)
        _description.postValue(description)
        _labelTitle.postValue(labelTitle)
        _toolbarTitle.postValue(toolbarTitle)
        _shouldHideProgress.postValue(shouldHideProgress)
        _list.postValue(recyclerList)
    }

    override fun onCleared() {
        super.onCleared()
    }

    fun howToLoadData(bundle: Bundle) {
        when (bundle.getInt("type")) {
            ListType.PLAYLIST_LIST -> {
                bundle.getParcelable<Playlist>("list_detail")?.let { playlist ->
                    val map = HashMap<String, String>().apply {
                        put("id", playlist.id)
                    }
                    model?.getTopListById(map, { list ->
                        refreshView(
                            recyclerList = list.tracks,
                            appbarImageUrl = playlist.imageUrl,
                            creatorName = list.creator.nickname,
                            labelTitle = playlist.name,
                            toolbarTitle = "排行榜",
                            description = list.description,
                            shouldHideProgress = true
                        )
                    }, {

                    })
                }
            }

            ListType.RANKING_LIST -> {
                bundle.getParcelable<ListDetail>("list_detail")?.let { listInfo ->
                    val map = HashMap<String, String>().apply {
                        put("id", listInfo.id!!)
                    }
                    model?.getTopListById(map, { list ->
                        refreshView(
                            recyclerList = list.tracks,
                            appbarImageUrl = listInfo.coverImgUrl,
                            creatorName = list.creator.nickname,
                            labelTitle = listInfo.name!!,
                            toolbarTitle = "排行榜",
                            description = listInfo.description!!,
                            shouldHideProgress = true
                        )
                    }, {

                    })
                }
            }

            ListType.ALBUM_LIST -> {
                bundle.getParcelable<SingleMonthData>("list_detail")?.let { singleWeekData ->
                    model?.getAlbumListById(singleWeekData.id, { albumDetail ->

                        var artists = ""
                        albumDetail.albumInfo.artists.forEach {
                            artists += it.name
                            if (albumDetail.albumInfo.artists.lastIndex != albumDetail.albumInfo.artists.indexOf(
                                    it
                                )
                            ) {
                                artists += "，"
                            }
                        }

                        refreshView(
                            recyclerList = albumDetail.songs,
                            appbarImageUrl = singleWeekData.imageUrl,
                            creatorName = artists,
                            labelTitle = singleWeekData.name,
                            toolbarTitle = "专辑",
                            description = albumDetail.albumInfo.description,
                            shouldHideProgress = true
                        )
                    }, {

                    })
                }
            }

            else -> {

            }
        }
    }
}