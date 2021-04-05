package com.example.graduationdesign.model.bean.song_list_bean

import com.google.gson.annotations.SerializedName

data class RecommendSongsBean(
    @SerializedName("code")
    var code: Int,

    @SerializedName("data")
    var dailySongs: DailySongs
)

data class DailySongs(
    @SerializedName("dailySongs")
    var songs: ArrayList<SongBean>
)
