package com.example.graduationdesign.model

class SearchType {
    companion object{
        //默认为 1 即单曲 , 取值意义 : 1: 单曲, 10: 专辑, 100: 歌手, 1000: 歌单, 1002: 用户, 1004: MV, 1006: 歌词, 1009: 电台, 1014: 视频, 1018:综合
        const val SINGLE = 1
        const val ALBUM = 10
        const val ARTIST = 100
        const val PLAYLIST = 1000
        const val COMPREHENSIVE = 1018
        const val VIDEO = 1004
    }
}