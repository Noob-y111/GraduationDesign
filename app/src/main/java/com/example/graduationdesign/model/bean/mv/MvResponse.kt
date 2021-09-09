package com.example.graduationdesign.model.bean.mv

import com.example.graduationdesign.model.bean.song_list_bean.ArtistBean
import com.google.gson.annotations.SerializedName

data class MvResponse(
    var code: Int,
    @SerializedName("data", alternate = ["mvs"])
    var data: ArrayList<MvBean>,
    var hasMore: Boolean,
    var updateTime: Long
)

data class MvUrlResponse(
    var code: Int,
    var data: MvUrl
)

data class MvDetailResponse(
    var code: Int,
    var data: MvDetailInfo
)

data class MvDetailInfo(
    @SerializedName("vid", alternate = ["id"])
    var vid: String,
    @SerializedName("title", alternate = ["name"])
    var title: String,
    @SerializedName("coverUrl", alternate = ["cover"])
    var coverUrl: String,
    @SerializedName("playTime", alternate = ["playCount"])
    var playTime: String,
    @SerializedName("creator", alternate = ["artists"])
    var artists: ArrayList<ArtistBean>,
    @SerializedName("desc")
    var description: String,
    @SerializedName("publishTime")
    var publishTime: String,
    @SerializedName("subCount")
    var subCount: Int,
    @SerializedName("shareCount")
    var shareCount: Int,
    @SerializedName("commentCount")
    var commentCount: Int,
    @SerializedName("duration")
    var duration: Int

)

data class MvUrl(
    var id: String,
    var url: String,
    @SerializedName("r")
    var resolution: Int
)

data class MvBean(
    @SerializedName("vid", alternate = ["id"])
    var vid: String,
    @SerializedName("title", alternate = ["name"])
    var title: String,
    @SerializedName("coverUrl", alternate = ["cover"])
    var coverUrl: String,
    @SerializedName("playTime", alternate = ["playCount"])
    var playTime: String,
    @SerializedName("creator", alternate = ["artists"])
    var artists: ArrayList<ArtistBean>,
    @SerializedName("lastRank")
    var lastRank: Int?
)

data class VideoListResponse(
    var code: Int,
    @SerializedName("hasmore")
    var hasMore: Boolean,
    var datas: ArrayList<VideoData>
)

data class VideoData(
    @SerializedName("data")
    var video: VideoBean
)

data class VideoBean(
    @SerializedName("vid", alternate = ["id"])
    var vid: String,
    @SerializedName("title", alternate = ["name"])
    var title: String,
    @SerializedName("coverUrl", alternate = ["cover"])
    var coverUrl: String,
    @SerializedName("playTime", alternate = ["playCount"])
    var playTime: String,
    @SerializedName("description")
    var description: String,
    @SerializedName("creator")
    var creator: Creator?,
    var durationms: Int
)

data class Creator(
    var nickname: String,
    var backgroundUrl: String?,
    var avatarUrl: String?,
    var province: String,
    var city: String
)
