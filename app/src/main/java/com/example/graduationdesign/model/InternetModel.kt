package com.example.graduationdesign.model

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.android.volley.Request
import com.android.volley.VolleyError
import com.example.graduationdesign.callback.BaseBehaviorCallBack
import com.example.graduationdesign.costume.StringPostRequest
import com.example.graduationdesign.model.bean.*
import com.example.graduationdesign.model.bean.new_album_bean.SingleMonthData
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
import com.example.graduationdesign.model.bean.song_list_bean.RecommendSongsBean
import com.example.graduationdesign.model.bean.song_list_bean.SongBean
import com.example.graduationdesign.tools.api.ApiTools
import com.example.graduationdesign.tools.JudgeVolleyError
import com.example.graduationdesign.tools.api.RetrofitApi
import com.google.gson.Gson
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.concurrent.thread

class InternetModel constructor(private val context: Context) {

    private val volley = VolleySingleInstance.getInstance(context)

    private val retrofit = Retrofit.Builder().baseUrl(RetrofitApi.baseUrl)
        .addConverterFactory(GsonConverterFactory.create()).build()
    private val request = retrofit.create(RetrofitApi::class.java)

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
        success: ArrayList<ArrayList<ImageAndText>>.() -> Unit,
        errorHandling: (error: String?) -> Unit
    ) {
        val call = request.getRecommendNewSong()
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                thread {
                    response.body()?.string()?.let {
                        JSONObject(it).also { json ->
                            when (val code = json.getInt("code")) {
                                200 -> {
                                    val result = json.getJSONArray("result")
                                    val list = ArrayList<ArrayList<ImageAndText>>()
                                    val threeList = ArrayList<ImageAndText>()
                                    for (i in 0 until result.length()) {
                                        val item = result.getJSONObject(i)
                                        val id = item.getString("id")
                                        val imageUrl = item.getString("picUrl")
                                        val text = item.getString("name")
                                        threeList.add(
                                            ImageAndText(
                                                imageUrl = imageUrl,
                                                text = text,
                                                id = id,
                                                type = TypeOfMusicEnum.SONG
                                            )
                                        )
                                        if ((i % 3 == 0) and (i != 0)) {
                                            val subList = ArrayList<ImageAndText>().also { sub ->
                                                sub.addAll(threeList)
                                            }
                                            threeList.clear()
                                            list.add(subList)
                                        }
                                    }
                                    success(list)
                                }

                                else -> {

                                }
                            }
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

    fun getUserPlayList(uid: String) {
        val map = HashMap<String, String>()
        map["uid"] = uid
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
        baseBehavior(map, ApiTools.getPlaylistByTag(), object : BaseBehaviorCallBack{
            override fun dealWithJSON(json: String) {
                Gson().fromJson(json, ListSpecies::class.java).also {
                    block(it)
                }
            }

            override fun errorBehavior(error: VolleyError) {
                println("================error.message: ${error.message}")
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
                when(val code = response.body()?.code){
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
        baseBehavior(map, ApiTools.getPlaylistDetailById(), object : BaseBehaviorCallBack{
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
        id: String,
        block: (albumDetail: AlbumDetail) -> Unit,
        errorHandling: (error: String?) -> Unit
    ) {
        baseBehavior(HashMap<String, String>().apply {
            put("id", id)
        }, ApiTools.getAlbumDetailById(), object : BaseBehaviorCallBack {
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
