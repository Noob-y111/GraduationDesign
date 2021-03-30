package com.example.graduationdesign.view.main

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.*
import com.example.graduationdesign.base.BaseFragment
import com.example.graduationdesign.model.InternetModel
import com.example.graduationdesign.model.bean.ImageOfBanner
import com.example.graduationdesign.model.bean.User
import com.example.graduationdesign.tools.JudgeVolleyError
import java.util.*
import kotlin.collections.ArrayList
import com.example.graduationdesign.R
import com.example.graduationdesign.model.bean.ImageAndText
import com.example.graduationdesign.model.bean.TypeOfMusicEnum
import okio.utf8Size
import org.json.JSONObject
import kotlin.concurrent.thread

class MainActivityViewModel : ViewModel() {
    private var model: InternetModel? = null

    companion object {
        private var viewModel: MainActivityViewModel? = null
        fun newInstance(lifecycleOwner: ViewModelStoreOwner): MainActivityViewModel =
            synchronized(this) {
                return@synchronized if (viewModel != null) {
                    viewModel!!
                } else {
                    viewModel =
                        ViewModelProvider(lifecycleOwner).get(MainActivityViewModel::class.java)
                    viewModel!!
                }
            }
    }

    private var fragmentList = ArrayList<BaseFragment>()

    fun setFragmentList(list: ArrayList<BaseFragment>) {
        fragmentList.addAll(list)
    }

    fun getFragmentListSize() = fragmentList.size
    fun getFragmentByPosition(position: Int) = fragmentList[position]

    //all
    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user

    private val _toastString = MutableLiveData<String>()
    val toastString: LiveData<String> = _toastString

    fun showMessage(message: String) {
        _toastString.postValue(message)
    }

    fun getUser(bundle: Bundle) {
        val user = User(
            bundle.getString("uid"),
            bundle.getString("name"),
            bundle.getString("head"),
            bundle.getString("background"),
            bundle.getString("token"),
            bundle.getString("cookie"),
        )
        println("================user: $user")
        _user.postValue(user)
    }

    //local
    fun getInfo() {
        _user.value?.uid?.let { model?.getUserPlayList(it) }
    }

    //music club
    private val _bannerImageList =
        MutableLiveData<ArrayList<ImageOfBanner>>(ArrayList<ImageOfBanner>())
    val bannerImageList: LiveData<ArrayList<ImageOfBanner>> = _bannerImageList

    private val _adviceImageList =
        MutableLiveData<ArrayList<ImageAndText>>()
    val adviceImageList: LiveData<ArrayList<ImageAndText>> = _adviceImageList

    private val _adviceNewestAlbums =
        MutableLiveData<ArrayList<ImageAndText>>()
    val adviceNewestAlbums: LiveData<ArrayList<ImageAndText>> = _adviceNewestAlbums

    private val _currentBannerIndex = MutableLiveData<Int>()
    val currentBannerIndex: LiveData<Int> = _currentBannerIndex

    private val _currentBannerIndexWithAnimation = MutableLiveData<Int>()
    val currentBannerIndexWithAnimation: LiveData<Int> = _currentBannerIndexWithAnimation

    private var position: Int = -1
    private var timerIsStarted = false

    private var timer: Timer? = null
    private var timerTask: TimerTask? = null

    fun cancelTimer() {
        timer?.cancel()
        timerTask?.cancel()
        timer = null
        timerTask = null
        timerIsStarted = false
    }

    fun initTimer() {
        if (_bannerImageList.value?.size == 0) return
        timer = Timer()
        timerTask = object : TimerTask() {
            override fun run() {
                position = (position + 1) % (_bannerImageList.value?.size?.plus(2)!!)
                _currentBannerIndexWithAnimation.postValue(position)
            }
        }
        startScroll()
    }

    fun setDataModel(context: Context) {
        model = InternetModel(context)
    }

    fun startScroll() {
        if (!timerIsStarted) {
            timerIsStarted = true
            timer?.schedule(timerTask, 5000, 5000)
        }
    }

    fun getScaleHeight(width: Int): Float {
        return (420f / 1080f) * width
    }

    fun dragEnded(state: Int) {
        if (state == 0) {
            judgeBannerIndexIsEndOrStart(this.position)
        }
    }

    fun setPosition(position: Int) {
        this.position = position
    }

    private fun submitListOfPlaylist(list: ArrayList<ImageAndText>) {
        _adviceImageList.postValue(list)
    }

    private fun submitListOfNewestAlbums(list: ArrayList<ImageAndText>) {
        _adviceNewestAlbums.postValue(list)
    }

//    private fun judgeBannerIndex(position: Int): Int{
//        if (position == -1 ) return -1
//        val count = _bannerImageList.value?.size?.plus(2)
//        println("================position: $position")
//        return if (count != null) {
//            when (position) {
//                0 -> count - 2
//                count - 1 -> 1
//                else -> position
//            }
//        } else{
//            -1
//        }
//    }

    private fun judgeBannerIndexIsEndOrStart(position: Int) {
        if (position == -1) return
        val count = _bannerImageList.value?.size?.plus(2)
        if (count != null) {
            when (position) {
                0 -> _currentBannerIndex.postValue(count - 2)
                count - 1 -> _currentBannerIndex.postValue(1)
            }
        }
    }

    fun getTestList() {
        _bannerImageList.postValue(ArrayList<ImageOfBanner>().also {
            it.add(ImageOfBanner("http://p1.music.126.net/KDD-VY7mCz-VRI83VHW6kA==/109951165840043101.jpg"))
            it.add(ImageOfBanner("http://p1.music.126.net/-ItirBQmXa7iGB_fov3i7A==/109951165840625600.jpg"))
            it.add(ImageOfBanner("http://p1.music.126.net/0dHl0ciJw-8FfkIZQZLltg==/109951165840467848.jpg"))
            it.add(ImageOfBanner("http://p1.music.126.net/TShMxRgXpTDjbRTMgCaT0g==/109951165840502226.jpg"))
            it.add(ImageOfBanner("http://p1.music.126.net/KjZNRMkV0oKzdS1dBFkFKg==/109951165840045824.jpg"))
        })
    }

    fun getNewestAlbum() {
        model?.getNewestAlbum({
            thread {
                it?.let {
                    JSONObject(it).also { jsonObject ->
                        when (val code = jsonObject.getInt("code")) {
                            200 -> {
                                val albums = jsonObject.getJSONArray("albums")
                                val length = albums.length()
                                val list = ArrayList<ImageAndText>()
                                for (i in 0 until length) {
                                    val album = albums.getJSONObject(i)
                                    val imageUrl = album.getString("picUrl")
                                    val text = album.getString("name")
                                    val id = album.getString("id")
                                    list.add(ImageAndText(imageUrl, text, id, TypeOfMusicEnum.SONG))
                                    println("================list[i]: ${list[i]}")
                                    if (i >= 5) break
                                }
                                submitListOfNewestAlbums(list)
                            }

                            else -> {
                                showMessage("错误类型：$code")
                            }
                        }
                    }
                }
            }
        }, {
            it?.let { it1 -> showMessage(it1) }
        })
    }

    fun getRecommendPlaylist() {
        user.value?.let { it ->
            model?.getRecommendPlaylist(it.uid!!, it.cookie!!,
                {
                    thread {
                        it?.let {
                            JSONObject(it).also { json ->
                                when (val code = json.getInt("code")) {
                                    200 -> {
                                        val recommend = json.getJSONArray("recommend")
                                        val playlist = ArrayList<ImageAndText>()
                                        for (i in 0 until recommend.length()) {
                                            val aImageAndText = recommend.getJSONObject(i)
                                            val id = aImageAndText.getString("id")
                                            val imageUrl = aImageAndText.getString("picUrl")
                                            val text = aImageAndText.getString("name")
                                            playlist.add(
                                                ImageAndText(
                                                    imageUrl,
                                                    text,
                                                    id,
                                                    TypeOfMusicEnum.PLAYLIST
                                                )
                                            )
                                            println("================playlist[i]: ${playlist[i]}")
                                            if (i >= 5) break
                                        }
                                        submitListOfPlaylist(playlist)
                                    }

                                    else -> {
                                        showMessage("错误类型：$code")
                                    }
                                }
                            }
                        }
                    }
                }, {
                    showMessage(it!!)
                })
        }
    }

    fun getBannerList() {
        model?.getBannerList({
            _bannerImageList.postValue(it)
        }, {
            println("=======================失败:")
            val str = JudgeVolleyError.judgeVolleyError(it)
            println("================str: $str")
        })
    }


    override fun onCleared() {
        super.onCleared()
        cancelTimer()
    }
}