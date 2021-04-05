package com.example.graduationdesign.view.playlist.subview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.example.graduationdesign.model.InternetModel
import com.example.graduationdesign.model.bean.playlist_bean.Playlist
import com.example.graduationdesign.model.bean.playlist_bean.Tag
import com.example.graduationdesign.view.playlist.adapter.PlaylistAdapter

class SubFragmentViewModel : ViewModel() {
    private var model: InternetModel? = null

    private var offset = 0
    private var tag: Tag? = null
    var isLoading = false
    private var playlistCount = 0

    private val _playlists = MutableLiveData<ArrayList<Playlist>>().also {
        it.postValue(ArrayList())
    }
    val playlists: LiveData<ArrayList<Playlist>> = _playlists

    private val _footerState = MutableLiveData<PlaylistAdapter.FooterState>()
    val footerState: LiveData<PlaylistAdapter.FooterState> = _footerState

    fun getInternetModel(internetModel: InternetModel) {
        model = internetModel
    }

    private fun changeState(state: PlaylistAdapter.FooterState){
        _footerState.postValue(state)
    }

    private fun showPlaylist(list: ArrayList<Playlist>) {
        val newList = _playlists.value
        newList?.addAll(list)
        _playlists.value = newList
        offset = newList!!.size
    }

    fun pagingLoading(loadingEnable: Boolean, state: Int){
        if (loadingEnable){
            if (state == RecyclerView.SCROLL_STATE_IDLE){
                changeState(PlaylistAdapter.FooterState.LOADING)
                pagingRequest()
            }
        }
    }

    private fun putParameters(): HashMap<String, String> {
        HashMap<String, String>().also {
            it["cat"] = tag!!.name
            it["offset"] = offset.toString()
            return it
        }
    }

    fun firstRequest(tag: Tag) {
        this.tag = tag
        getPlaylistByTag(putParameters())
    }

    private fun pagingRequest() {
        getPlaylistByTag(putParameters())
    }

    private fun getPlaylistByTag(map: HashMap<String, String>) {
        if (isLoading) return else isLoading = true
        model?.getPlaylistByTag(map, {
            this?.let {
                showPlaylist(it.playlists)
            }
        }, {
            println("================${tag!!.name}: $it")
        })
    }
}