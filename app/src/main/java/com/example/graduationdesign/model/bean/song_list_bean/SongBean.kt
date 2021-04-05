package com.example.graduationdesign.model.bean.song_list_bean

import com.google.gson.annotations.SerializedName

data class SongBean(

    @SerializedName("id")
    var id: String,

    @SerializedName("name")
    var name: String,

    @SerializedName(value = "ar", alternate = ["artists"])
    var artists: ArrayList<ArtistBean>,

    @SerializedName(value = "al", alternate = ["album"])
    var album: AlbumBean
)
