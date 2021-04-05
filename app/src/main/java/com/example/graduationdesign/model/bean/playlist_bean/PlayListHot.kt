package com.example.graduationdesign.model.bean.playlist_bean

import com.google.gson.annotations.SerializedName

data class PlayListHot(
    @SerializedName("code")
    val code: Int,

    @SerializedName("tags")
    var tags: ArrayList<Tag>
)

data class Tag(
    @SerializedName("id")
    var id: String,
    @SerializedName("name")
    var name: String,
    @SerializedName("createTime")
    var createTime: String,
    @SerializedName("type")
    var type: Int,
    @SerializedName("category")
    var category: Int,
    @SerializedName("usedCount")
    var usedCount: Int
)
