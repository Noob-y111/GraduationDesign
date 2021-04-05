package com.example.graduationdesign.model.bean.playlist_detail_bean

import com.example.graduationdesign.model.bean.song_list_bean.ArtistBean
import com.example.graduationdesign.model.bean.song_list_bean.SongBean
import com.google.gson.annotations.SerializedName

data class AlbumDetail(
    @SerializedName("code")
    var code: Int,
    @SerializedName("songs")
    var songs: ArrayList<SongBean>,
    @SerializedName("album")
    var albumInfo: AlbumInfo
)

data class AlbumInfo(
    @SerializedName("id")
    var id: String,

    @SerializedName("name")
    var name: String,

    @SerializedName("picUrl")
    var picUrl: Any,

    @SerializedName("description")
    var description: String,

    @SerializedName(value = "ar", alternate = ["artists"])
    var artists: ArrayList<ArtistBean>,
)
