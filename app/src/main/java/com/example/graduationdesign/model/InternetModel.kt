package com.example.graduationdesign.model

import android.content.Context
import androidx.lifecycle.MutableLiveData
import cn.leancloud.gson.GSONConverterFactory
import com.android.volley.Request
import com.android.volley.VolleyError
import com.example.graduationdesign.callback.BaseBehaviorCallBack
import com.example.graduationdesign.costume.StringPostRequest
import com.example.graduationdesign.model.bean.ImageAndText
import com.example.graduationdesign.model.bean.ImageOfBanner
import com.example.graduationdesign.tools.api.ApiTools
import com.example.graduationdesign.tools.JudgeVolleyError
import com.example.graduationdesign.tools.api.RetrofitApi
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
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
                        println("================code: ${code}")
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
                println("================json: ${json}")
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
                println("================json: $json")
                block(json)
            }

            override fun errorBehavior(error: VolleyError) {
                println("================error.message: ${error.message}")
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
                println("================json: $json")
                thread {
                    JSONObject(json).apply {
                        val banner = getJSONArray("banners")
                        val bannerList = ArrayList<ImageOfBanner>()
                        for (i in 0 until banner.length()) {
                            val aBanner = banner.getJSONObject(i)
                            val pic = aBanner.getString("pic")
                            println("================pic: $pic")
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
        call.enqueue(object : Callback<ResponseBody>{
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                success(response.body()?.string())
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
}
