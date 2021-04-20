package com.example.graduationdesign.model.bean.user_info

import com.example.graduationdesign.model.bean.playlist_bean.Playlist

data class PlaylistResponse(
    var code: Int,
    var more: Boolean,
    var playlist: ArrayList<Playlist>
)
