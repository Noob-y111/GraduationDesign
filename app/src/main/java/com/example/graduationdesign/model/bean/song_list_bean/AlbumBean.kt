package com.example.graduationdesign.model.bean.song_list_bean

import com.google.gson.annotations.SerializedName

data class AlbumBean(

    @SerializedName("id")
    var id: String,

    @SerializedName("name")
    var name: String,

    @SerializedName("picUrl")
    var picUrl: Any
)
