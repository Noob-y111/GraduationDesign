package com.example.graduationdesign.tools.api

class ApiTools {
    companion object{
        private const val baseUrl = "http://192.168.43.101:3000"
        fun cellphoneCheckUrl() = "$baseUrl/cellphone/existence/check?timestamp=${System.currentTimeMillis()}"
        fun getVerificationCode() = "$baseUrl/captcha/sent?timestamp=${System.currentTimeMillis()}"
        fun validationVerificationCode() = "$baseUrl/captcha/verify?timestamp=${System.currentTimeMillis()}"
        fun registeredByPhone() = "$baseUrl/register/cellphone?timestamp=${System.currentTimeMillis()}"
        fun loginByPhone() = "$baseUrl/login/cellphone?timestamp=${System.currentTimeMillis()}"
        fun loginByEmail() = "$baseUrl/login?timestamp=${System.currentTimeMillis()}"
        fun loginOut() = "$baseUrl/logout?timestamp=${System.currentTimeMillis()}"
        fun getBanner() = "$baseUrl/banner?type=1&timestamp=${System.currentTimeMillis()}"
        fun userInformation() = "$baseUrl/user/subcount?timestamp=${System.currentTimeMillis()}"
        fun recommendedSongsDaily() = "$baseUrl/recommend/songs?timestamp=${System.currentTimeMillis()}"
        fun recommendedPlaylistDaily() = "$baseUrl/recommend/resource?timestamp=${System.currentTimeMillis()}"
        fun getNewestAlbum() = "$baseUrl/album/newest?timestamp=${System.currentTimeMillis()}"

        fun getAlbumDetailById() = "$baseUrl/album?timestamp=${System.currentTimeMillis()}"
        fun getPlaylistDetailById() = "$baseUrl/playlist/detail?timestamp=${System.currentTimeMillis()}"
        fun getPlaylistByTag() = "$baseUrl/top/playlist?limit=30&timestamp=${System.currentTimeMillis()}"
        fun isTheMusicAvailable() = "$baseUrl/check/music?timestamp=${System.currentTimeMillis()}"
        fun getMusicUrl() = "$baseUrl/song/url?timestamp=${System.currentTimeMillis()}"

        //search
        fun searchDefault() = "$baseUrl/search/default?timestamp=${System.currentTimeMillis()}"
        fun searchHotDetailList() = "$baseUrl/search/hot/detail?timestamp=${System.currentTimeMillis()}"
        fun searchSuggestList() = "$baseUrl/search/suggest?type=mobile&timestamp=${System.currentTimeMillis()}"
        fun searchResultByKeyword() = "$baseUrl/search?timestamp=${System.currentTimeMillis()}"

        //explore
        fun mvRankingList() = "$baseUrl/top/mv?timestamp=${System.currentTimeMillis()}"
        fun mvUrl() = "$baseUrl/mv/url?r=1080&timestamp=${System.currentTimeMillis()}"
        fun mvDetailInfo() = "$baseUrl/mv/detail?timestamp=${System.currentTimeMillis()}"
        fun similarMv() = "$baseUrl/simi/mv?timestamp=${System.currentTimeMillis()}"
        fun videoList() = "$baseUrl/video/timeline/recommend?timestamp=${System.currentTimeMillis()}"
        fun similarVideoList() = "$baseUrl/related/allvideo?timestamp=${System.currentTimeMillis()}"

        //user info
        fun userDetail() = "$baseUrl/user/detail?timestamp=${System.currentTimeMillis()}"
        fun userAccount() = "$baseUrl/user/account?timestamp=${System.currentTimeMillis()}"
        fun userPlaylist() = "$baseUrl/user/playlist?timestamp=${System.currentTimeMillis()}"

        //play page
        fun getLyric() = "$baseUrl/lyric?timestamp=${System.currentTimeMillis()}"

        //list
        fun addAndDeleteFromId() = "$baseUrl/playlist/tracks?timestamp=${System.currentTimeMillis()}"
        fun updateUser() = "$baseUrl/user/update?timestamp=${System.currentTimeMillis()}"
    }
}