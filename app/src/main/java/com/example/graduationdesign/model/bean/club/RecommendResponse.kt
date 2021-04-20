package com.example.graduationdesign.model.bean.club

import com.google.gson.annotations.SerializedName

data class RecommendResponse(
    var code: Int,
    var recommend: ArrayList<ClubPlaylist>
)

data class ClubPlaylist(
    @SerializedName("id")
    var id: String,
    @SerializedName("name")
    var name: String,
    @SerializedName("userId")
    var userId: String?,
    @SerializedName("coverImgUrl", alternate = ["picUrl"])
    var imageUrl: String,
    @SerializedName("playCount", alternate = ["playcount"])
    var playCount: String
)
