package com.example.graduationdesign.model.bean.club_bean

import com.example.graduationdesign.model.bean.playlist_bean.Playlist

data class RecommendPlaylistResponse(
    var code: Int,
    var recommend: ArrayList<Playlist>
)
