package com.example.graduationdesign.view.main

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.*
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
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
import com.example.graduationdesign.model.bean.club_bean.NewAlbumResponse
import com.example.graduationdesign.model.bean.club_bean.RecommendPlaylistResponse
import com.example.graduationdesign.model.bean.new_album_bean.SingleMonthData
import com.example.graduationdesign.model.bean.playlist_bean.Playlist
import com.example.graduationdesign.model.bean.song_list_bean.SongBean
import com.example.graduationdesign.service.MyService
import com.example.graduationdesign.service.POSITION
import com.example.graduationdesign.service.SONG_LIST
import com.google.gson.Gson
import org.json.JSONObject
import kotlin.collections.HashMap
import kotlin.concurrent.thread

class MainActivityViewModel : ViewModel() {
    private var model: InternetModel? = null
    private var binder: MyService.MyBinder? = null

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

    fun setBinder(binder: MyService.MyBinder) {
        this.binder = binder
    }

    fun getBinder() = this.binder

    fun changeSongAndSongList(position: Int, songList: ArrayList<SongBean>) {
        binder?.startMusic(
            HashMap<String, Any>().also {
                it[POSITION] = position
                it[SONG_LIST] = songList
            }
        )
    }

    fun changeLocalSongAndSongList(position: Int, songList: ArrayList<SongBean>) {
        binder?.startLocalMusic(
            HashMap<String, Any>().also {
                it[POSITION] = position
                it[SONG_LIST] = songList
            }
        )
    }

    fun getFragmentListSize() = fragmentList.size
    fun getFragmentByPosition(position: Int) = fragmentList[position]

    //all
    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user

    private val _toastString = MutableLiveData<String>()
    val toastString: LiveData<String> = _toastString

    private val _hideBottomNavigationView = MutableLiveData(false)
    val hideBottomNavigationView: LiveData<Boolean> = _hideBottomNavigationView

    private fun showMessage(message: String) {
        _toastString.postValue(message)
    }

    fun updateUserSelf(){
        _user.value = _user.value
    }

    private fun setHideBottomNavigationView(boolean: Boolean) {
        _hideBottomNavigationView.postValue(boolean)
    }

    fun shouldHideView(id: Int) {
        when (id) {
            in intArrayOf(R.id.musicClubFragment, R.id.exploreFragment, R.id.localFragment) -> {
                if (_hideBottomNavigationView.value == false) {
                    return
                } else {
                    setHideBottomNavigationView(false)
                }
            }
            else -> {
                println("=======================导航变化:")
                if (_hideBottomNavigationView.value == true) {
                    return
                } else {
                    setHideBottomNavigationView(true)
                }
            }
        }
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


    // main activity
    private var mainFooterPosition = -1

    fun setMainFooterPosition(position: Int) {
        println("=======================我先运行:setMainFooterPosition")
        mainFooterPosition = position
    }

//    fun changeCurrentSong(position: Int, list: ArrayList<SongBean>){
//        if (position == mainFooterPosition) return
//        binder?.startMusic(HashMap<String, Any>().also {
//            it[POSITION] = position - 1
//            it[SONG_LIST] = list
//        })
//    }

//    fun hasTheSameContent(list1: ArrayList<SongBean>, list2: ArrayList<SongBean>): Boolean{
//        if (list1.size == list2.size){
//
//        }else{
//            return false
//        }
//    }

    private fun judgePosition() {
        binder?.let {
            it.service.currentSongListAndPosition.value?.let { map ->
                var size = (map["songList"] as ArrayList<SongBean>).size
                if (size == 1) return
                size += 2
                when (mainFooterPosition) {
                    0 -> {
                        binder?.changePosition(size - 3)
                    }

                    size - 1 -> {
                        binder?.changePosition(0)
                    }

                    else -> {
                        binder?.changePosition(mainFooterPosition - 1)
                    }
                }
            }
        }
    }

    fun mainFooterDragEnable(state: Int) {
        if (state == ViewPager2.SCROLL_STATE_IDLE) {
            println("=======================我先运行:mainFooterDragEnable")
            judgePosition()
        }
    }


    //local
    fun getInfo() {
        _user.value?.let {
            model?.getUserPlayList(it.uid!!, it.cookie!!)
        }
    }

    private val _localPlaylist = MutableLiveData<ArrayList<Playlist>>()
    val localPlaylist: LiveData<ArrayList<Playlist>> = _localPlaylist

    fun getUserPlaylist(user: User) {
        if (_localPlaylist.value != null) return
        model?.getUserPlaylistById(HashMap<String, String>().also {
            it["uid"] = user.uid!!
            it["cookie"] = user.cookie!!
        }, {
            _localPlaylist.postValue(this)
        }, {

        })
    }

    //music club
    private val _bannerImageList =
        MutableLiveData<ArrayList<ImageOfBanner>>(ArrayList<ImageOfBanner>())
    val bannerImageList: LiveData<ArrayList<ImageOfBanner>> = _bannerImageList

    private val _playlist =
        MutableLiveData<ArrayList<Playlist>>()
    val playlist: LiveData<ArrayList<Playlist>> = _playlist

    private val _adviceNewestAlbums =
        MutableLiveData<ArrayList<SingleMonthData>>()
    val adviceNewestAlbums: LiveData<ArrayList<SingleMonthData>> = _adviceNewestAlbums

    private val _adviceNewSong =
        MutableLiveData<ArrayList<SongBean>>()
    val adviceNewSong: LiveData<ArrayList<SongBean>> = _adviceNewSong

    private val _currentBannerIndex = MutableLiveData<Int>()
    val currentBannerIndex: LiveData<Int> = _currentBannerIndex

    private val _currentBannerIndexWithAnimation = MutableLiveData<Int>()
    val currentBannerIndexWithAnimation: LiveData<Int> = _currentBannerIndexWithAnimation

    private var bannerPosition: Int = -1
    private var timerIsStarted = false

    private var timer: Timer? = null
    private var timerTask: TimerTask? = null

    fun changeCurrentBannerIndex() {
        if (bannerPosition != -1) {
            _currentBannerIndex.postValue(bannerPosition)
        }
    }

    fun cancelTimer() {
        timer?.cancel()
        timerTask?.cancel()
        timer = null
        timerTask = null
        timerIsStarted = false
    }

    fun initTimer() {
        _bannerImageList.value?.let {
            if (it.size == 0 || it.size == 1)
                return
            timer ?: kotlin.run {
                timer = Timer()
                timerTask = object : TimerTask() {
                    override fun run() {
                        bannerPosition =
                            (bannerPosition + 1) % (it.size.plus(2))
                        _currentBannerIndexWithAnimation.postValue(bannerPosition)
                    }
                }
            }
            startScroll()
        }
    }

    fun setDataModel(context: Context) {
        model = InternetModel(context)
    }

    fun getDataModel() = model

    private fun startScroll() {
        if (!timerIsStarted) {
            timerIsStarted = true
            timer?.schedule(timerTask, 5000, 5000)
        }
    }

    fun getScaleHeight(width: Int): Float {
        return (420f / 1080f) * width
    }

    fun dragEnded(state: Int) {
        if (state == RecyclerView.SCROLL_STATE_IDLE) {
            judgeBannerIndexIsEndOrStart(this.bannerPosition)
        }
    }

    fun setPosition(position: Int) {
        this.bannerPosition = position
    }

    private fun submitListOfPlaylist(list: ArrayList<Playlist>) {
        _playlist.postValue(list)
    }

    private fun submitListOfNewestAlbums(list: ArrayList<SingleMonthData>) {
        _adviceNewestAlbums.postValue(list)
    }

    private fun submitListOfSongItem(list: ArrayList<SongBean>) {
        _adviceNewSong.postValue(list)
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
                    Gson().fromJson(it, NewAlbumResponse::class.java).also { response ->
                        if (response.code == 200) {
                            submitListOfNewestAlbums(response.albums)
                        } else {
                            showMessage("错误类型：${response.code}")
                        }
                    }
                }
            }
        }, {
            it?.let { it1 -> showMessage(it1) }
        })
    }

    fun getRecommendNewSong() {
        model?.getRecommendNewSong({
            submitListOfSongItem(this)
        }, {
            showMessage(it!!)
        })
    }

    fun getRecommendPlaylist() {
        user.value?.let { it ->
            model?.getRecommendPlaylist(it.uid!!, it.cookie!!,
                {
                    it?.let { string ->
                        Gson().fromJson(string, RecommendPlaylistResponse::class.java)
                            .also { response ->
                                if (response.code == 200) {
                                    submitListOfPlaylist(response.recommend)
                                } else {
                                    showMessage("错误类型：${response.code}")
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