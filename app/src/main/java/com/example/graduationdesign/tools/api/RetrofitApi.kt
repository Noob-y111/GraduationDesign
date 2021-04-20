package com.example.graduationdesign.tools.api

import com.example.graduationdesign.model.bean.new_album_bean.TheNewDiscShelvesBean
import com.example.graduationdesign.model.bean.playlist_bean.ListSpecies
import com.example.graduationdesign.model.bean.playlist_bean.PlayListHot
import com.example.graduationdesign.model.bean.playlist_detail_bean.AlbumDetail
import com.example.graduationdesign.model.bean.playlist_detail_bean.PlaylistDetailById
import com.example.graduationdesign.model.bean.ranking_list_bean.ListDetail
import com.example.graduationdesign.model.bean.ranking_list_bean.TopListDetail
import com.example.graduationdesign.model.bean.song_list_bean.RecommendSongsBean
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface RetrofitApi {
    companion object{
        const val baseUrl = "http://192.168.31.64:3000"
    }

    @POST("/recommend/resource?")
    fun recommendedPlaylistDaily( @Body map: Map<String, String>): Call<ResponseBody>

    @POST("/album/newest?")
    fun getNewestAlbum(): Call<ResponseBody>

    //推荐新音乐
    @GET("/personalized/newsong")
    fun getRecommendNewSong(): Call<ResponseBody>

    //新碟上架
    @GET("/top/album?limit=30")
    fun getTopAlbum(@QueryMap map: Map<String, String>): Call<TheNewDiscShelvesBean>

    @POST("/recommend/songs?")
    fun getRecommendDailySong(@Body map: Map<String, String>): Call<RecommendSongsBean>

    @GET("/playlist/hot")
    fun getHotPlaylist(): Call<PlayListHot>

    @POST("/top/playlist?limit=30")
    fun getPlaylistByTag(@Body map: Map<String, String>): Call<ListSpecies>

    @GET("/toplist/detail")
    fun getTopListDetail(@Header("timestamp") time: String): Call<TopListDetail>

    @POST("/playlist/detail?")
    fun getPlaylistDetailById(@Body map: Map<String, String>)

    @POST("/album?")
    fun getAlbumDetailById(@Body map: Map<String, String>): Call<AlbumDetail>

    @POST("/playlist/detail?")
    fun getTopListDetailById(@Body map: Map<String, String>): Call<PlaylistDetailById>
}