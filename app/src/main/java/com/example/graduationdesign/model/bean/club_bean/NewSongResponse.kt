package com.example.graduationdesign.model.bean.club_bean

import com.example.graduationdesign.model.bean.song_list_bean.SongBean

data class NewSongResponse(
    var code: Int,
    var result: ArrayList<ResultOfNewSong>
)

data class ResultOfNewSong(
    var song: SongBean
)
