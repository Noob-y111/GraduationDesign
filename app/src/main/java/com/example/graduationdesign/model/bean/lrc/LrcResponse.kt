package com.example.graduationdesign.model.bean.lrc

import com.google.gson.annotations.SerializedName

data class LrcResponse(
    var code: Int,
    @SerializedName("lrc")
    var lrc: StringLrc
)

data class StringLrc(
    @SerializedName("lyric")
    var lrcStr: String
)

data class LyricBean(
    var lrc:String,
    var start: Int,
    var end: Int?
)
