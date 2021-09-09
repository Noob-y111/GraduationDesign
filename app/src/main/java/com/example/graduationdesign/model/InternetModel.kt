package com.example.graduationdesign.model

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.MutableLiveData
import com.android.volley.Request
import com.android.volley.VolleyError
import com.example.graduationdesign.R
import com.example.graduationdesign.callback.BaseBehaviorCallBack
import com.example.graduationdesign.costume.StringPostRequest
import com.example.graduationdesign.model.bean.*
import com.example.graduationdesign.model.bean.club_bean.NewSongResponse
import com.example.graduationdesign.model.bean.lrc.LrcResponse
import com.example.graduationdesign.model.bean.lrc.StringLrc
import com.example.graduationdesign.model.bean.mv.*
import com.example.graduationdesign.model.bean.mv.VideoData
import com.example.graduationdesign.model.bean.new_album_bean.TheNewDiscShelvesBean
import com.example.graduationdesign.model.bean.playlist_bean.ListSpecies
import com.example.graduationdesign.model.bean.playlist_bean.PlayListHot
import com.example.graduationdesign.model.bean.playlist_bean.Playlist
import com.example.graduationdesign.model.bean.playlist_bean.Tag
import com.example.graduationdesign.model.bean.playlist_detail_bean.AlbumDetail
import com.example.graduationdesign.model.bean.playlist_detail_bean.PlaylistDetailById
import com.example.graduationdesign.model.bean.playlist_detail_bean.PlaylistWithSongs
import com.example.graduationdesign.model.bean.ranking_list_bean.ListDetail
import com.example.graduationdesign.model.bean.ranking_list_bean.TopListDetail
import com.example.graduationdesign.model.bean.search_bean.*
import com.example.graduationdesign.model.bean.song_list_bean.AlbumBean
import com.example.graduationdesign.model.bean.song_list_bean.ArtistBean
import com.example.graduationdesign.model.bean.song_list_bean.RecommendSongsBean
import com.example.graduationdesign.model.bean.song_list_bean.SongBean
import com.example.graduationdesign.model.bean.user_info.PlaylistResponse
import com.example.graduationdesign.model.bean.user_info.UserDetailResponse
import com.example.graduationdesign.model.room.search.SearchHistory
import com.example.graduationdesign.tools.api.ApiTools
import com.example.graduationdesign.tools.JudgeVolleyError
import com.example.graduationdesign.tools.api.RetrofitApi
import com.google.gson.Gson
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.FileOutputStream
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.concurrent.thread

class InternetModel constructor(private val context: Context) {

    private val volley = VolleySingleInstance.getInstance(context)

    private val retrofit = Retrofit.Builder().baseUrl(RetrofitApi.baseUrl)
        .addConverterFactory(GsonConverterFactory.create()).build()
    private val request = retrofit.create(RetrofitApi::class.java)

    private val historyDao = AppDatabase.newInstance(context).historyDao()
    private val songBeanDao = AppDatabase.newInstance(context).songBeanDao()

    private fun baseBehavior(
        parameter: HashMap<String, String>?,
        api: String,
        callback: BaseBehaviorCallBack
    ) {
        val stringRequest = StringPostRequest(
            Request.Method.POST,
            api,
            {
                callback.dealWithJSON(it)
            },
            {
                callback.errorBehavior(it)
            }
        )
        parameter?.let { stringRequest.putValues(it) }
        volley.requestQueue.add(stringRequest)
    }

//    fun checkPhoneNumberIsExist(number: String, liveData: MutableLiveData<String>) {
//        val stringRequest = StringPostRequest(
//            Request.Method.POST,
//            ApiTools.cellphoneCheckUrl(),
//            {
//                println("================it: $it")
//                JSONObject(it).apply {
//                    val existId = getInt("exist")
//                    println("================existId: $existId")
//                    if (existId == 1){
//                        liveData.postValue("该手机号已经注册")
//                    }
//                }
//            },
//            {
//                println("================it: ${it.message}")
//            }
//        )
//        stringRequest.putValues("phone", number)
//        volley.requestQueue.add(stringRequest)
//    }

    fun checkPhoneNumberIsExist(
        number: String,
        liveData: MutableLiveData<String>,
        _registeredEnabled: MutableLiveData<Boolean>,
        block: () -> Unit
    ) {
        val map = HashMap<String, String>()
        map["phone"] = number
        baseBehavior(map, ApiTools.cellphoneCheckUrl(), object : BaseBehaviorCallBack {
            override fun dealWithJSON(json: String) {
                println("================checkPhoneNumberIsExist: $json")
                JSONObject(json).apply {
                    val existId = getInt("exist")
                    println("================existId: $existId")
                    if (existId == 1) {
                        _registeredEnabled.postValue(false)
                        liveData.postValue("该手机号已经注册")
                    } else {
                        _registeredEnabled.postValue(true)
                        block()
                    }
                }
            }

            override fun errorBehavior(error: VolleyError) {

            }
        })
    }

    fun validationVerificationCode(
        number: String,
        code: String,
        liveData: MutableLiveData<String>,
        block: (map: HashMap<String, String>) -> Unit
    ) {
        val map = HashMap<String, String>()
        map["phone"] = number
        map["captcha"] = code
        baseBehavior(map, ApiTools.validationVerificationCode(), object : BaseBehaviorCallBack {
            override fun dealWithJSON(json: String) {
                thread {
                    JSONObject(json).apply {
                        val returnCode = getInt("code")
                        if (returnCode == 200) {
//                            liveData.postValue("注册成功")
                            block(map)
                        } else {
                            liveData.postValue("注册失败")
                        }
                    }
                }
            }

            override fun errorBehavior(error: VolleyError) {

            }
        })
    }

    fun registeredByCellphone(map: HashMap<String, String>, liveData: MutableLiveData<String>) {
        val phoneNumber = map["phone"].toString()
        map["nickname"] = phoneNumber
        map["password"] = phoneNumber
        baseBehavior(map, ApiTools.registeredByPhone(), object : BaseBehaviorCallBack {
            override fun dealWithJSON(json: String) {
                thread {
                    println("================json: $json")
                    JSONObject(json).apply {
                        val code = getInt("code")
                        when (code) {
                            200 -> {
                                //todo
                                liveData.postValue("注册成功")
                            }

                            503 -> {
                                //todo
                                liveData.postValue("验证码错误")
                            }

                            else -> {
                                liveData.postValue("注册失败")
                            }
                        }
                    }
                }
            }

            override fun errorBehavior(error: VolleyError) {
                println("================error: ${error.message}")
            }
        })
    }

    fun getVerificationCode(number: String, liveData: MutableLiveData<String>) {
        val map = HashMap<String, String>()
        map["phone"] = number
        baseBehavior(map, ApiTools.getVerificationCode(), object : BaseBehaviorCallBack {
            override fun dealWithJSON(json: String) {
                thread {
                    JSONObject(json).apply {
                        val code = getInt("code")
                        if (code == 200) {
                            liveData.postValue("验证码发送成功")
                        } else {
                            liveData.postValue("验证码发送失败")
                        }
                    }
                }
            }

            override fun errorBehavior(error: VolleyError) {

            }
        })
    }

    fun loginOut() {
        baseBehavior(null, ApiTools.loginOut(), object : BaseBehaviorCallBack {
            override fun dealWithJSON(json: String) {
                TODO("Not yet implemented")
            }

            override fun errorBehavior(error: VolleyError) {
                TODO("Not yet implemented")
            }
        })
    }

    fun loginByEmail(
        map: HashMap<String, String>,
        block: (json: String) -> Unit,
        errorHandling: (error: VolleyError?) -> Unit
    ) {
        baseBehavior(map, ApiTools.loginByEmail(), object : BaseBehaviorCallBack {
            override fun dealWithJSON(json: String) {
                block(json)
            }

            override fun errorBehavior(error: VolleyError) {
                errorHandling(error)
            }

        })
    }

    fun loginByCellphone(
        map: HashMap<String, String>,
        block: (json: String) -> Unit,
        errorHandling: (error: VolleyError?) -> Unit
    ) {
        baseBehavior(map, ApiTools.loginByPhone(), object : BaseBehaviorCallBack {
            override fun dealWithJSON(json: String) {
                block(json)
            }

            override fun errorBehavior(error: VolleyError) {
                errorHandling(error)
            }
        })

    }


    fun getBannerList(
        success: (ArrayList<ImageOfBanner>) -> Unit,
        error: (error: VolleyError) -> Unit
    ) {
        baseBehavior(null, ApiTools.getBanner(), object : BaseBehaviorCallBack {
            override fun dealWithJSON(json: String) {
                thread {
                    JSONObject(json).apply {
                        val banner = getJSONArray("banners")
                        val bannerList = ArrayList<ImageOfBanner>()
                        for (i in 0 until banner.length()) {
                            val aBanner = banner.getJSONObject(i)
                            val pic = aBanner.getString("pic")
                            bannerList.add(ImageOfBanner(pic))
                        }
                        success(bannerList)
                    }
                }
            }

            override fun errorBehavior(error: VolleyError) {
                error(error)
            }
        })
    }

    fun getRecommendPlaylist(
        uid: String,
        cookies: String,
        success: (json: String?) -> Unit,
        errorHandling: (error: String?) -> Unit
    ) {
        val map = HashMap<String, String>()
        map["uid"] = uid
        map["cookie"] = cookies
        map["timestamp"] = System.currentTimeMillis().toString()
        val call = request.recommendedPlaylistDaily(map)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                success(response.body()?.string())
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                errorHandling(t.message)
            }
        })
    }

    fun getNewestAlbum(
        success: (json: String?) -> Unit,
        errorHandling: (error: String?) -> Unit
    ) {
        val call = request.getNewestAlbum()
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                success(response.body()?.string())
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                errorHandling(t.message)
            }
        })
    }

    fun getRecommendNewSong(
        success: ArrayList<SongBean>.() -> Unit,
        errorHandling: (error: String?) -> Unit
    ) {
        val call = request.getRecommendNewSong()
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {

                response.body()?.string()?.let {
                    Gson().fromJson(it, NewSongResponse::class.java).also { data ->
                        if (data.code == 200) {
                            val list = ArrayList<SongBean>()
                            data.result.forEachIndexed { index, resultOfNewSong ->
                                if (index < 9){
                                    list.add(resultOfNewSong.song)
                                }
                            }
                            success(list)
                        } else {
                            errorHandling("错误类型：${data.code}")
                        }
                    }
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                errorHandling(t.message)
            }
        })
    }


//    fun getRecommendPlaylist(
//        uid: String,
//        cookies: String,
//        success: (json: String) -> Unit,
//        errorHandling: (error: VolleyError?) -> Unit
//    ) {
//        val map = HashMap<String, String>()
//        map["uid"] = uid
//        map["cookie"] = cookies
//        baseBehavior(map, ApiTools.recommendedPlaylistDaily(), object : BaseBehaviorCallBack {
//            override fun dealWithJSON(json: String) {
//                println("=======================success:")
//                success(json)
//            }
//
//            override fun errorBehavior(error: VolleyError) {
//                val string = JudgeVolleyError.judgeVolleyError(error)
//                println("================string: $string")
//                errorHandling(error)
//            }
//        })
//    }

//    fun getNewestAlbum(
//        success: (json: String) -> Unit,
//        errorHandling: (error: VolleyError?) -> Unit
//    ) {
//        baseBehavior(null, ApiTools.getNewestAlbum(), object : BaseBehaviorCallBack {
//            override fun dealWithJSON(json: String) {
//                success(json)
//            }
//
//            override fun errorBehavior(error: VolleyError) {
//                errorHandling(error)
//            }
//        })
//    }

    fun getUserPlayList(uid: String, cookies: String) {
        val map = HashMap<String, String>()
        map["uid"] = uid
        map["cookie"] = cookies
        baseBehavior(map, ApiTools.userInformation(), object : BaseBehaviorCallBack {
            override fun dealWithJSON(json: String) {
                println("================json: $json")
            }

            override fun errorBehavior(error: VolleyError) {
                val string = JudgeVolleyError.judgeVolleyError(error)
                println("================string: ${string}")
            }
        })
    }

    //    song list data
    fun getRecommendDailySong(
        map: HashMap<String, String>,
        block: ArrayList<SongBean>?.() -> Unit,
        errorHandling: (error: String?) -> Unit
    ) {
        val call = request.getRecommendDailySong(map)
        call.enqueue(object : Callback<RecommendSongsBean> {
            override fun onResponse(
                call: Call<RecommendSongsBean>,
                response: Response<RecommendSongsBean>
            ) {
                block(response.body()?.dailySongs?.songs)
            }

            override fun onFailure(call: Call<RecommendSongsBean>, t: Throwable) {
                errorHandling(t.message)
            }

        })
    }

    //    playlist data
    fun getHotPlaylistTag(
        block: ArrayList<Tag>?.() -> Unit,
        errorHandling: (error: String?) -> Unit
    ) {
        val call = request.getHotPlaylist()
        call.enqueue(object : Callback<PlayListHot> {
            override fun onResponse(call: Call<PlayListHot>, response: Response<PlayListHot>) {
                block(response.body()?.tags)
            }

            override fun onFailure(call: Call<PlayListHot>, t: Throwable) {
                errorHandling(t.message)
            }
        })
    }

    fun getPlaylistByTag(
        map: HashMap<String, String>,
        block: ListSpecies?.() -> Unit,
        errorHandling: (error: String?) -> Unit
    ) {
        println("================map[\"cat\"]: ${map["cat"]}")
        baseBehavior(map, ApiTools.getPlaylistByTag(), object : BaseBehaviorCallBack {
            override fun dealWithJSON(json: String) {
                Gson().fromJson(json, ListSpecies::class.java).also {
                    block(it)
                }
            }

            override fun errorBehavior(error: VolleyError) {
                println("================error.message: ${error.message}")
                errorHandling(error.message)
            }
        })
//        val call = request.getPlaylistByTag(map)
//        call.enqueue(object : Callback<ListSpecies> {
//            override fun onResponse(call: Call<ListSpecies>, response: Response<ListSpecies>) {
//                block(response.body()?.playlists)
//            }
//
//            override fun onFailure(call: Call<ListSpecies>, t: Throwable) {
//                errorHandling(t.message)
//            }
//        })
    }

    // ranking list data
    fun getRankingDetail(
        block: (officialList: ArrayList<ListDetail>, otherList: ArrayList<ListDetail>) -> Unit,
        errorHandling: (error: String?) -> Unit
    ) {
        val call = request.getTopListDetail(System.currentTimeMillis().toString())
        call.enqueue(object : Callback<TopListDetail> {
            override fun onResponse(call: Call<TopListDetail>, response: Response<TopListDetail>) {
                thread {
                    println("================response.body()?.code: ${response.body()?.code}")
                    val officialList = ArrayList<ListDetail>()
                    val otherList = ArrayList<ListDetail>()
                    response.body()?.listDetail?.forEach {
                        if (it.tracks.size == 0) {
                            otherList.add(it)
                        } else {
                            officialList.add(it)
                        }
                    }
                    block(officialList, otherList)
                }
            }

            override fun onFailure(call: Call<TopListDetail>, t: Throwable) {
                println("================t: ${t.message}")
            }
        })
    }

    //new album fragment
    fun getTheNewDiscShelves(
        map: HashMap<String, String>,
        block: (theNewDiscShelvesBean: TheNewDiscShelvesBean) -> Unit,
        errorHandling: (error: String?) -> Unit
    ) {
        val call = request.getTopAlbum(map)
        call.enqueue(object : Callback<TheNewDiscShelvesBean> {
            override fun onResponse(
                call: Call<TheNewDiscShelvesBean>,
                response: Response<TheNewDiscShelvesBean>
            ) {
                when (val code = response.body()?.code) {
                    200 -> {
                        response.body()?.let {
                            block(it)
                        }
                    }

                    else -> {
                        println("================code: $code")
                    }
                }
            }

            override fun onFailure(call: Call<TheNewDiscShelvesBean>, t: Throwable) {
                println("================it: ${t.message}")
            }

        })
    }

    //reuse fragment
    fun getTopListById(
        map: HashMap<String, String>,
        block: (playlistWithSongs: PlaylistWithSongs) -> Unit,
        errorHandling: (error: String?) -> Unit
    ) {
        baseBehavior(map, ApiTools.getPlaylistDetailById(), object : BaseBehaviorCallBack {
            override fun dealWithJSON(json: String) {
                println("================json: $json")
                Gson().fromJson(json, PlaylistDetailById::class.java).also {
                    block(it.playlist)
                }
            }

            override fun errorBehavior(error: VolleyError) {
                println("================error.message: ${error.message}")
            }
        })
    }

    fun getAlbumListById(
        map: HashMap<String, String>,
        block: (albumDetail: AlbumDetail) -> Unit,
        errorHandling: (error: String?) -> Unit
    ) {
        baseBehavior(map, ApiTools.getAlbumDetailById(), object : BaseBehaviorCallBack {
            override fun dealWithJSON(json: String) {
                println("================json: $json")
                Gson().fromJson(json, AlbumDetail::class.java).also {
                    block(it)
                }
            }

            override fun errorBehavior(error: VolleyError) {
                println("================error.message: ${error.message}")
            }
        })
    }

    //service
    fun isTheMusicAvailable(id: String, callback: BaseBehaviorCallBack) {
        baseBehavior(
            HashMap<String, String>().also { it["id"] = id },
            ApiTools.isTheMusicAvailable(),
            callback
        )
    }

    fun getMusicUrl(id: String, callback: BaseBehaviorCallBack) {
        baseBehavior(
            HashMap<String, String>().also { it["id"] = id },
            ApiTools.getMusicUrl(),
            callback
        )
    }

    //search
    fun saveHistory(keyword: String) {
        GlobalScope.launch {
            historyDao.addHistory(SearchHistory( keyword))
        }
    }

    fun getHistory(block: (list: ArrayList<SearchHistory>) -> Unit) {
        GlobalScope.launch {
            block(historyDao.getAllHistory() as ArrayList<SearchHistory>)
        }
    }

    fun deleteHistory(){
        GlobalScope.launch {
            historyDao.deleteAll()
        }
    }

    fun getDefaultKeyWord(
        block: (data: Data) -> Unit,
        errorHandling: (error: VolleyError?) -> Unit
    ) {
        baseBehavior(null, ApiTools.searchDefault(), object : BaseBehaviorCallBack {
            override fun dealWithJSON(json: String) {
                Gson().fromJson(json, SearchDetail::class.java).also {
                    if (it.code == 200) {
                        block(it.data)
                    }
                }
            }

            override fun errorBehavior(error: VolleyError) {
                errorHandling(error)
            }
        })
    }

    fun getHotListDetail(
        block: (list: ArrayList<HotItem>) -> Unit,
        errorHandling: (error: VolleyError?) -> Unit
    ) {
        baseBehavior(null, ApiTools.searchHotDetailList(), object : BaseBehaviorCallBack {
            override fun dealWithJSON(json: String) {
                Gson().fromJson(json, HotDetail::class.java).also {
                    if (it.code == 200) {
                        block(it.data)
                    }
                }
            }

            override fun errorBehavior(error: VolleyError) {
                errorHandling(error)
            }
        })
    }

    fun getSearchSuggest(
        map: HashMap<String, String>,
        block: (result: Result) -> Unit,
        errorHandling: (error: VolleyError?) -> Unit
    ) {
        baseBehavior(map, ApiTools.searchSuggestList(), object : BaseBehaviorCallBack {
            override fun dealWithJSON(json: String) {
                println("================json: $json")
                Gson().fromJson(json, SuggestResult::class.java).also {
                    if (it.code == 200) {
                        block(it.result)
                    }
                }
            }

            override fun errorBehavior(error: VolleyError) {
                errorHandling(error)
            }

        })
    }

    fun getSearchResultByKeyword(
        map: HashMap<String, String>,
        block: (result: Results) -> Unit,
        errorHandling: (error: VolleyError?) -> Unit
    ) {
        baseBehavior(map, ApiTools.searchResultByKeyword(), object : BaseBehaviorCallBack {
            override fun dealWithJSON(json: String) {
                Gson().fromJson(json, SearchResultAll::class.java).also {
                    if (it.code == 200) {
                        block(it.result)
                    }
                }
            }

            override fun errorBehavior(error: VolleyError) {
                errorHandling(error)
            }
        })
    }

    fun getOtherSearchResultByKeyword(
        map: HashMap<String, String>,
        block: (json: String) -> Unit,
        errorHandling: (error: VolleyError?) -> Unit
    ) {
        baseBehavior(map, ApiTools.searchResultByKeyword(), object : BaseBehaviorCallBack {
            override fun dealWithJSON(json: String) {
                block(json)
            }

            override fun errorBehavior(error: VolleyError) {
                errorHandling(error)
            }
        })
    }

    //explore
    fun mvRankingListByArea(
        map: HashMap<String, String>,
        block: (map: HashMap<String, Any>) -> Unit,
        errorHandling: (error: VolleyError?) -> Unit
    ) {
        baseBehavior(map, ApiTools.mvRankingList(), object : BaseBehaviorCallBack {
            override fun dealWithJSON(json: String) {
                Gson().fromJson(json, MvResponse::class.java).also {
                    if (it.code == 200) {
                        block(HashMap<String, Any>().apply {
                            this["time"] = it.updateTime
                            this["list"] = it.data
                        })
                    }
                }
            }

            override fun errorBehavior(error: VolleyError) {
                errorHandling(error)
            }
        })
    }

    fun getMvInfoById(
        vid: String,
        block: (mvInfo: MvDetailInfo) -> Unit,
        errorHandling: (error: VolleyError?) -> Unit
    ) {
        baseBehavior(HashMap<String, String>().apply {
            this["mvid"] = vid
        }, ApiTools.mvDetailInfo(), object : BaseBehaviorCallBack {
            override fun dealWithJSON(json: String) {
                Gson().fromJson(json, MvDetailResponse::class.java).also {
                    if (it.code == 200) {
                        block(it.data)
                    }
                }
            }

            override fun errorBehavior(error: VolleyError) {
                errorHandling(error)
            }
        })
    }

    fun getMvUrlById(
        vid: String,
        block: (mvUrl: MvUrl) -> Unit,
        errorHandling: (error: VolleyError?) -> Unit
    ) {
        baseBehavior(HashMap<String, String>().apply {
            this["id"] = vid
        }, ApiTools.mvUrl(), object : BaseBehaviorCallBack {
            override fun dealWithJSON(json: String) {
                Gson().fromJson(json, MvUrlResponse::class.java).also {
                    if (it.code == 200) {
                        block(it.data)
                    }
                }
            }

            override fun errorBehavior(error: VolleyError) {
                errorHandling(error)
            }
        })
    }

    fun getSimilarMv(
        vid: String,
        success: (mvList: ArrayList<MvBean>) -> Unit,
        errorHandling: (error: VolleyError?) -> Unit
    ) {
        baseBehavior(HashMap<String, String>().apply {
            this["mvid"] = vid
        }, ApiTools.similarMv(), object : BaseBehaviorCallBack {
            override fun dealWithJSON(json: String) {
                Gson().fromJson(json, MvResponse::class.java).also {
                    if (it.code == 200) {
                        success(it.data)
                    }
                }
            }

            override fun errorBehavior(error: VolleyError) {
                errorHandling(error)
            }

        })
    }

    fun getVideoList(
        map: HashMap<String, String>,
        block: (list: ArrayList<VideoData>) -> Unit,
        errorHandling: (error: VolleyError?) -> Unit
    ) {
        baseBehavior(map, ApiTools.videoList(), object : BaseBehaviorCallBack {
            override fun dealWithJSON(json: String) {
                Gson().fromJson(json, VideoListResponse::class.java).also {
                    if (it.code == 200) {
                        block(it.datas)
                    }
                }
            }

            override fun errorBehavior(error: VolleyError) {
                errorHandling(error)
            }
        })
    }

    //local music
    private fun getAlbumImage(albumId: String): Any {
        val uri = "content://media/external/audio/albums"
        val selector = arrayOf("album_art")
        val albumCursor = context.contentResolver.query(
            Uri.parse("$uri/${albumId}"),
            selector,
            null, null, null
        )
        albumCursor?.let {
            if (it.count >= 1 && it.columnCount >= 1) {
                it.moveToNext()
                val albumImage = it.getString(0)
                it.close()
                return albumImage ?: R.drawable.player_bg
            }
        }
        albumCursor?.close()
        return R.drawable.player_bg
    }

    fun getLocalMusic(
        block: ArrayList<SongBean>.() -> Unit,
        errorHandling: (error: String?) -> Unit
    ) {
        thread {
            val cursor = context.contentResolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null,
                null,
                null,
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER
            )
            cursor ?: kotlin.run {
                errorHandling("还没有歌曲")
            }
            cursor?.let {
                val songList = ArrayList<SongBean>()
                while (it.moveToNext()) {
                    val id = it.getLong(it.getColumnIndexOrThrow(MediaStore.Audio.Media._ID))
                    val name =
                        it.getString(it.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME))
                    val artistId =
                        it.getString(it.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST_ID))
                    val artistName =
                        it.getString(it.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST))
                    val albumId =
                        it.getString(it.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID))
                    val albumName =
                        it.getString(it.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM))
                    val artist = ArtistBean(artistId, artistName)
                    val album = AlbumBean(albumId, albumName, getAlbumImage(albumId))
                    val song = SongBean(
                        id.toString(),
                        name,
                        ArrayList<ArtistBean>().also { list -> list.add(artist) },
                        album
                    )
                    println("================song: $song")
                    songList.add(song)
                }
                block(songList)
            }
            cursor?.close()
        }
    }

    //user info
    fun getUserDetail(
        map: HashMap<String, String>,
        block: (detail: UserDetailResponse) -> Unit,
        errorHandling: (error: VolleyError?) -> Unit
    ) {
        baseBehavior(map, ApiTools.userDetail(), object : BaseBehaviorCallBack {
            override fun dealWithJSON(json: String) {
                Gson().fromJson(json, UserDetailResponse::class.java).also {
                    if (it.code == 200) {
                        block(it)
                    }
                }
            }

            override fun errorBehavior(error: VolleyError) {
                errorHandling(error)
            }
        })
    }

    fun updateUser(map: HashMap<String, String>, block: () -> Unit, errorHandling: (error: String?) -> Unit){
        baseBehavior(map, ApiTools.updateUser(), object : BaseBehaviorCallBack{
            override fun dealWithJSON(json: String) {
                if (JSONObject(json).getString("code") == "200"){
                    block()
                }
            }

            override fun errorBehavior(error: VolleyError) {
                println("================error.networkResponse.data: ${String(error.networkResponse.data)}")
                errorHandling(JSONObject(String(error.networkResponse.data)).getString("msg"))
            }
        })
    }

    fun getUserPlaylistById(
        map: HashMap<String, String>,
        block: ArrayList<Playlist>.() -> Unit,
        errorHandling: (error: VolleyError?) -> Unit
    ) {
        baseBehavior(map, ApiTools.userPlaylist(), object : BaseBehaviorCallBack {
            override fun dealWithJSON(json: String) {
                Gson().fromJson(json, PlaylistResponse::class.java).also {
                    if (it.code == 200) {
                        block(it.playlist)
                    }
                }
            }

            override fun errorBehavior(error: VolleyError) {
                errorHandling(error)
            }

        })
    }

    fun updateAvatar(user: User, bitmap: Bitmap) {
        try {
            thread {
                val path = context.filesDir.path + "images"
                val file = File(path)
                val output = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output)
                output.flush()
                output.close()

                val body =
                    file.asRequestBody("Content-Type': 'multipart/form-data".toMediaTypeOrNull())
                val part = MultipartBody.Part.create(body)
                val call = request.uploadAvatar(part, HashMap<String, String>().also { map ->
                    map["timestamp"] = System.currentTimeMillis().toString()
                    map["cookie"] = user.cookie!!
                })
                call.enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>
                    ) {
                        println(
                            "================response.body()?.string(): ${
                                response.body()?.string()
                            }"
                        )
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        t.printStackTrace()
                    }

                })
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    //play page
    fun getLyric(
        map: HashMap<String, String>,
        block: (str: StringLrc?) -> Unit,
        errorHandling: (error: VolleyError?) -> Unit
    ) {
        baseBehavior(map, ApiTools.getLyric(), object : BaseBehaviorCallBack {
            override fun dealWithJSON(json: String) {
                Gson().fromJson(json, LrcResponse::class.java).also {
                    if (it.code == 200) {
                        block(it.lrc)
                    }
                }
            }

            override fun errorBehavior(error: VolleyError) {
                errorHandling(error)
            }
        })
    }

    //play history
    fun savePlayHistory(songBean: SongBean){
        GlobalScope.launch {
            songBeanDao.insert(songBean)
        }
    }

    fun deletePlayHistory(songBean: SongBean){
        GlobalScope.launch {
            songBeanDao.delete(songBean)
        }
    }

    fun getPlayHistory(block: (list: ArrayList<SongBean>) -> Unit){
        GlobalScope.launch {
            block(songBeanDao.queryList() as ArrayList<SongBean>)
        }
    }

    fun deletePlayHistoryAll(){
        GlobalScope.launch {
            songBeanDao.deleteAll()
        }
    }

    //list
    fun addToList(map: HashMap<String, String>){
        baseBehavior(map, ApiTools.addAndDeleteFromId() + "&op=add", object : BaseBehaviorCallBack{
            override fun dealWithJSON(json: String) {
                println("================json: $json")
            }

            override fun errorBehavior(error: VolleyError) {

            }
        })
    }

    fun deleteFromList(){

    }

//    fun getRecommendDailySong(
//        map: HashMap<String, String>,
//        block: ArrayList<SongBean>?.() -> Unit,
//        errorHandling: (error: String?) -> Unit
//    ) {
//        val call = request.getRecommendDailySong(map)
//        call.enqueue(object : Callback<ResponseBody> {
//            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
//                val str = response.body()?.string()
//                println("================str: $str")
//            }
//
//            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
//
//            }
//
//        })
//    }
}
