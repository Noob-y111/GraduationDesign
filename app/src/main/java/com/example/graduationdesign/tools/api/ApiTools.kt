package com.example.graduationdesign.tools.api

class ApiTools {
    companion object{
        private const val baseUrl = "http://192.168.31.128:3000"
        fun cellphoneCheckUrl() = "$baseUrl/cellphone/existence/check?timestamp=${System.currentTimeMillis()}"
        fun getVerificationCode() = "$baseUrl/captcha/sent?timestamp=${System.currentTimeMillis()}"
        fun validationVerificationCode() = "$baseUrl/captcha/verify?timestamp=${System.currentTimeMillis()}"
        fun registeredByPhone() = "$baseUrl/register/cellphone?timestamp=${System.currentTimeMillis()}"
        fun loginByPhone() = "$baseUrl/login/cellphone?timestamp=${System.currentTimeMillis()}"
        fun loginByEmail() = "$baseUrl/login?timestamp=${System.currentTimeMillis()}"
        fun getBanner() = "$baseUrl/banner?type=1&timestamp=${System.currentTimeMillis()}"
        fun userPlaylist() = "$baseUrl/user/playlist?timestamp=${System.currentTimeMillis()}"
        fun userInformation() = "$baseUrl/user/subcount?timestamp=${System.currentTimeMillis()}"
        fun recommendedSongsDaily() = "$baseUrl/recommend/songs?timestamp=${System.currentTimeMillis()}"
        fun recommendedPlaylistDaily() = "$baseUrl/recommend/resource?timestamp=${System.currentTimeMillis()}"
        fun getNewestAlbum() = "$baseUrl/album/newest?timestamp=${System.currentTimeMillis()}"
    }
}