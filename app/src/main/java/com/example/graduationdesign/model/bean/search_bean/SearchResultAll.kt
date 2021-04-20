package com.example.graduationdesign.model.bean.search_bean

import com.example.graduationdesign.model.bean.playlist_bean.Playlist
import com.example.graduationdesign.model.bean.playlist_detail_bean.Creator
import com.example.graduationdesign.model.bean.song_list_bean.AlbumBean
import com.example.graduationdesign.model.bean.song_list_bean.ArtistBean
import com.example.graduationdesign.model.bean.song_list_bean.SongBean
import com.google.gson.annotations.SerializedName

data class SearchResultAll(
    var code: Int,
    var result: Results
)

data class Results(
    @SerializedName("song")
    var songs: Songs,
    @SerializedName("playList")
    var playlists: Playlists,
    @SerializedName("artist")
    var artists: Artists,
    @SerializedName("album")
    var albums: Albums,
    @SerializedName("video")
    var videos: Videos
)

data class Songs(
    var songs: ArrayList<SongBean>,
)

data class Playlists(
    @SerializedName("playLists", alternate = ["playlists"])
    var playlists: ArrayList<Playlist>,
)

data class Artists(
    var artists: ArrayList<CompleteArtistBean>,
)

data class Albums(
    var albums: ArrayList<CompleteAlbumBean>,
)

data class Videos(
    @SerializedName("videos", alternate = ["mvs"])
    var videos: ArrayList<VideoBean>,
)

data class CompleteArtistBean(
    var id: String,
    var name: String,
    var picUrl: String,
)

data class CompleteAlbumBean(
    @SerializedName("id")
    var id: String,

    @SerializedName("name")
    var name: String,

    @SerializedName("picUrl")
    var picUrl: Any,

    @SerializedName("publishTime")
    var publishTime: Long,

    @SerializedName("artists")
    var artist: ArrayList<ArtistBean>
)

data class SimpleCreator(
    @SerializedName("userId", alternate = ["id"])
    var userId: String,
    @SerializedName("userName", alternate = ["name"])
    var userName: String
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
    @SerializedName("durationms", alternate = ["duration"])
    var durationms: Int,
    @SerializedName("creator", alternate = ["artists"])
    var creator: ArrayList<SimpleCreator>
)

data class ResultOfSingle(
    var code: Int,
    var result: Songs
)

data class ResultOfAlbum(
    var code: Int,
    var result: Albums
)

data class ResultOfPlaylist(
    var code: Int,
    var result: Playlists
)

data class ResultOfArtist(
    var code: Int,
    var result: Artists
)

data class ResultOfVideo(
    var code: Int,
    var result: Videos
)
