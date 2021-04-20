package com.example.graduationdesign.view.video

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.graduationdesign.R
import com.example.graduationdesign.model.InternetModel
import com.example.graduationdesign.model.bean.mv.MvBean
import com.example.graduationdesign.model.bean.mv.MvDetailInfo
import com.example.graduationdesign.view.video.player.MyMediaPlayer
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

class VideoViewModel : ViewModel() {

    private var onVideoSizeChangedBlock: ((width: Int, height: Int) -> Unit)? = null
    private var resumeOrPauseService: ((playOrPause: Boolean) -> Unit)? = null
    private var playerHasResource = false

    private val videoMediaPlayer = MyMediaPlayer().also { player ->
        player.setOnPreparedListener {
            it.start()
            _controllerImage.postValue(R.drawable.pause)
            playerHasResource = true
            resumeOrPauseService?.let { block -> block(true) }
            _videoDuration.postValue(player.duration)
            _progressEnable.postValue(false)
            changeToolbarVisibility(View.INVISIBLE)
            initTimer()
        }

        player.setOnVideoSizeChangedListener { _, width, height ->
            onVideoSizeChangedBlock?.let { it(width, height) }
        }

        player.setOnBufferingUpdateListener { _, percent ->
            _bufferedPosition.postValue(_currentMv.value?.duration?.times((percent.toFloat() / 100).toInt()))
        }
    }

    private var model: InternetModel? = null

    private val _currentMvId = MutableLiveData<String>()
    val currentMvId: LiveData<String> = _currentMvId

    private val _currentMv = MutableLiveData<MvDetailInfo>()
    val currentMv: LiveData<MvDetailInfo> = _currentMv

    private val _videoPosition = MutableLiveData<Int>(0)
    val videoPosition: LiveData<Int> = _videoPosition

    private val _bufferedPosition = MutableLiveData<Int>(0)
    val bufferedPosition: LiveData<Int> = _bufferedPosition

    private val _videoDuration = MutableLiveData<Int>(0)
    val videoDuration: LiveData<Int> = _videoDuration

    private val _progressEnable = MutableLiveData(true)
    val progressEnable: LiveData<Boolean> = _progressEnable

    private val _shouldToolbarShow = MutableLiveData(View.VISIBLE)
    val shouldToolbarShow: LiveData<Int> = _shouldToolbarShow

    private val _similarMvList = MutableLiveData<ArrayList<MvBean>>()
    val similarMvList: LiveData<ArrayList<MvBean>> = _similarMvList

    private val _controllerImage = MutableLiveData(R.drawable.play)
    val controllerImage: LiveData<Int> = _controllerImage

    fun addLifecycleObservable(block: (player: MyMediaPlayer) -> Unit) {
        block(videoMediaPlayer)
    }

    fun setOnVideoSizeChangedBlock(block: (width: Int, height: Int) -> Unit) {
        this.onVideoSizeChangedBlock = block
    }

    fun setResumeOrPauseService(block: (playOrPause: Boolean) -> Unit) {
        this.resumeOrPauseService = block
    }

    fun seekTo(msc: Int) {
        videoMediaPlayer.seekTo(msc)
    }

    private var timer: Timer? = null
    private var timerTask: TimerTask? = null

    private fun initTimer() {
        timer ?: kotlin.run {
            timer = Timer()
            timerTask = object : TimerTask() {
                override fun run() {
                    _videoPosition.postValue(videoMediaPlayer.currentPosition)
                }
            }
            timer?.schedule(timerTask, 0, 100)
        }
    }

    fun cancelTimer() {
        timer?.cancel()
        timerTask?.cancel()
        timer = null
        timerTask = null
    }

    fun playerIsPlayOrPause(){
        if (playerHasResource) {
            if (videoMediaPlayer.isPlaying) {
                videoMediaPlayer.pause()
                _controllerImage.postValue(R.drawable.play)
            } else {
                videoMediaPlayer.start()
                _controllerImage.postValue(R.drawable.pause)
            }
        }
    }

    fun getPlayer() = videoMediaPlayer

    fun setModel(model: InternetModel?) {
        this.model = model
    }

    private fun changeCurrentMv(info: MvDetailInfo) {
        _currentMv.postValue(info)
    }

    fun changeCurrentId(vid: String) {
        println("================vid: $vid")
        _currentMvId.value = vid
    }

    private fun changeToolbarVisibility(visibility: Int) {
        _shouldToolbarShow.postValue(visibility)
    }

    private var controllerShowTime = 0L

    fun surfaceClick() {
        _shouldToolbarShow.value?.let {
            if (it == View.INVISIBLE) {
                changeToolbarVisibility(View.VISIBLE)
                controllerShowTime = System.currentTimeMillis()
                viewModelScope.launch {
                    delay(3000)
                    if (System.currentTimeMillis() - controllerShowTime >= 3000) {
                        _shouldToolbarShow.value = View.INVISIBLE
                    }
                }
            } else {
                changeToolbarVisibility(View.INVISIBLE)
            }
        }
    }

    fun getVideoUrl(vid: String) {
        model?.getMvUrlById(vid, {
            loadVideo(it.url)
        }, {

        })
    }

    fun videoInfo(vid: String) {
        model?.getMvInfoById(vid, {
            changeCurrentMv(it)
        }, {

        })
    }

    private fun loadVideo(url: String) {
        videoMediaPlayer.reset()
        videoMediaPlayer.setDataSource(url, true)
        videoMediaPlayer.isLooping = true
        videoMediaPlayer.prepareAsync()
    }

    fun loadSimilarMvById(vid: String) {
        model?.getSimilarMv(vid, {
            _similarMvList.postValue(it)
        }, {

        })
    }

    override fun onCleared() {
        super.onCleared()
        println("=======================video model destroy:")
        cancelTimer()
        playerHasResource = false
        resumeOrPauseService?.let { it(false) }
        videoMediaPlayer.release()
    }
}