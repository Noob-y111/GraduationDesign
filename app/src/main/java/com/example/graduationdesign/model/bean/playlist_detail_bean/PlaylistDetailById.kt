package com.example.graduationdesign.model.bean.playlist_detail_bean

import com.example.graduationdesign.model.bean.song_list_bean.SongBean
import com.google.gson.annotations.SerializedName

data class PlaylistDetailById(
    @SerializedName("code")
    var code: Int,
    @SerializedName("playlist")
    var playlist: PlaylistWithSongs
)

data class Creator(
    @SerializedName("userId")
    var userId: String,
    @SerializedName("avatarUrl")
    var avatarUrl: String,
    @SerializedName("nickname")
    var nickname: String,
    @SerializedName("backgroundUrl")
    var backgroundUrl: String,
    @SerializedName("signature")
    var signature: String,
    @SerializedName("description")
    var description: String
)

data class PlaylistWithSongs(
    @SerializedName("id")
    var id: String,
    @SerializedName("name")
    var name: String,
    @SerializedName("userId")
    var userId: String,
    @SerializedName("coverImgUrl")
    var imageUrl: String,
    @SerializedName("playCount")
    var playCount: String,
    @SerializedName("tracks")
    var tracks: ArrayList<SongBean>,
    @SerializedName("creator")
    var creator: Creator,
    @SerializedName("description")
    var description: String
)
