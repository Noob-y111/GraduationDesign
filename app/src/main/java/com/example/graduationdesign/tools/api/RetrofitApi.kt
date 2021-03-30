package com.example.graduationdesign.tools.api

import com.example.graduationdesign.model.bean.ImageAndText
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface RetrofitApi {
    companion object{
        const val baseUrl = "http://192.168.31.128:3000"
    }

    @POST("/recommend/resource?")
    fun recommendedPlaylistDaily( @Body map: Map<String, String>): Call<ResponseBody>

    @POST("/album/newest?")
    fun getNewestAlbum(): Call<ResponseBody>
}