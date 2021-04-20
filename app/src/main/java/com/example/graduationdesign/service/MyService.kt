package com.example.graduationdesign.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.MediaPlayer
import android.net.Uri
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.android.volley.VolleyError
import com.example.graduationdesign.R
import com.example.graduationdesign.callback.BaseBehaviorCallBack
import com.example.graduationdesign.model.InternetModel
import com.example.graduationdesign.model.bean.song_list_bean.SongBean
import com.example.graduationdesign.view.reuse.adapter.ReuseListAdapter
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.concurrent.thread

const val POSITION = "position"
const val SONG_LIST = "songList"

class MyService : LifecycleService() {

    companion object {
        private const val CHANNEL_ID = "my player channel id"
    }

    private lateinit var mediaPlayer: MediaPlayer
    private val model = InternetModel(this)
    val currentSongListAndPosition = MutableLiveData<HashMap<String, Any>>()
    val progressBarDuration = MutableLiveData<Int>(0)
    val progressBarPosition = MutableLiveData<Int>(0)
    val canFooterShow = MutableLiveData(false)
    var progressBarBuffer = MutableLiveData<Int>(0)
    val stopOrResumeMediaPlayer = MutableLiveData<Boolean>()
    private var type = ReuseListAdapter.Type.NONE


    private lateinit var remoteView: RemoteViews


    private var mediaHasResource = false

    private var timer: Timer? = null
    private var timerTask: TimerTask? = null

    private fun runTimerTask() {
        timer ?: kotlin.run {
            timer = Timer()
            timerTask = object : TimerTask() {
                override fun run() {
                    progressBarPosition.postValue(getMediaPlayerCurrentPosition())
                }
            }
        }
        timer?.schedule(timerTask, 0, 100)
    }

    private fun cancelTimerTask() {
        timer?.cancel()
        timerTask?.cancel()
        timer = null
        timerTask = null
    }

    override fun onCreate() {
        super.onCreate()
        executeForeground()
        mediaPlayer = MediaPlayer().also { media ->
            media.setOnCompletionListener {
                cancelTimerTask()
                it.reset()
                mediaHasResource = false
                updateMediaPlayerState()
                next()
            }

            media.setOnPreparedListener {
                it.start()
                mediaHasResource = true
                setProgressDuration()
                runTimerTask()
                updateMediaPlayerState()
            }

            media.setOnErrorListener { mp, what, extra ->

                return@setOnErrorListener false
            }

            media.setOnBufferingUpdateListener { mp, percent ->
                progressBarBuffer.postValue((mp.duration * (percent.toFloat() / 100)).toInt())
            }
        }
        liveDataObserve()
    }

    private fun getMediaPlayerCurrentPosition() = mediaPlayer.currentPosition
    private fun getMediaPlayerDuration() = mediaPlayer.duration

    private fun setProgressDuration() {
        progressBarDuration.postValue(getMediaPlayerDuration())
    }

    private fun executeForeground() {
        createChannel()
        remoteView = RemoteViews(packageName, R.layout.layout_notification_small).also {
            it.setTextViewText(R.id.tv_notification_name, "我先试试行不行")
            it.setOnClickPendingIntent(
                R.id.iv_notification_next_small,
                PendingIntent.getBroadcast(
                    this,
                    0,
                    Intent("notification_click"),
                    PendingIntent.FLAG_CANCEL_CURRENT
                )
            )
        }
        startForeground(1, newNotification("我先试试"))
    }

    private fun createChannel() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            val name = "this is my player channel name"
            val importance = NotificationManager.IMPORTANCE_LOW
            val channel = NotificationChannel(CHANNEL_ID, name, importance)
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun liveDataObserve() {
        currentSongListAndPosition.observe(this, {
            canFooterShow.postValue(true)
            val position = it[POSITION] as Int
            val list = it[SONG_LIST] as ArrayList<*>
            val song = list[position] as SongBean

            when (type) {
                ReuseListAdapter.Type.LOCAL -> {
                    startLocalMusicById(song.id)
                }

                ReuseListAdapter.Type.INTERNET -> {
                    model.isTheMusicAvailable(song.id, object : BaseBehaviorCallBack {
                        override fun dealWithJSON(json: String) {
                            thread {
                                JSONObject(json).also { jsonObject ->
                                    if (jsonObject.getBoolean("success")) {
                                        urlIsAvailable(song.id)
                                    } else {
                                        println("=======================cuowu:")
                                    }
                                }
                            }
                        }

                        override fun errorBehavior(error: VolleyError) {

                        }
                    })
                }
                else -> {}
            }
        })
    }

    private fun urlIsAvailable(songId: String) {
        model.getMusicUrl(songId, object : BaseBehaviorCallBack {
            override fun dealWithJSON(json: String) {
                JSONObject(json).also {
                    val code = it.getInt("code")
                    if (code == 200) {
                        val data = it.getJSONArray("data")
                        val url = data.getJSONObject(0).getString("url")
                        MainScope().launch {
                            startMusicByStringUrl(url)
                        }
                    }
                }
            }

            override fun errorBehavior(error: VolleyError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun startMusicByStringUrl(path: String) {
        cancelTimerTask()
        mediaPlayer.reset()
        mediaPlayer.setDataSource(path)
        mediaPlayer.prepareAsync()
    }

    private fun startLocalMusicById(id: String){
        cancelTimerTask()
        mediaPlayer.reset()
        mediaPlayer.setDataSource(application, Uri.parse("content://media/external/audio/media/$id"))
        mediaPlayer.prepareAsync()
    }

    private fun next() {
        currentSongListAndPosition.value?.let {
            val position = it[POSITION] as Int
            val size = (it[SONG_LIST] as ArrayList<*>).size

            if (position + 1 <= size - 1) {
                changeIndex(position + 1)
            } else {
                changeIndex(0)
            }
        }
    }

    private fun last() {
        currentSongListAndPosition.value?.let {
            val position = it[POSITION] as Int
            val size = (it[SONG_LIST] as ArrayList<*>).size

            if (position - 1 >= 0) {
                changeIndex(position - 1)
            } else {
                changeIndex(size - 1)
            }
        }
    }

    private fun changeIndex(position: Int) {
        currentSongListAndPosition.value?.let {
            if (position == it[POSITION]) {
                return
            }
            currentSongListAndPosition.value?.set(POSITION, position)
            currentSongListAndPosition.value = currentSongListAndPosition.value
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
        stopForeground(true)
    }

    override fun onBind(intent: Intent): IBinder {
        super.onBind(intent)
        return MyBinder()
    }

    fun mediaPlayerPlayOrPause() {
        if (mediaHasResource) {
            if (mediaPlayer.isPlaying) {
                mediaPlayer.pause()
                stopOrResumeMediaPlayer.postValue(true)
            } else {
                mediaPlayer.start()
                stopOrResumeMediaPlayer.postValue(false)
            }
        }
    }

    private fun updateMediaPlayerState() {
        if (mediaPlayer.isPlaying) {
            stopOrResumeMediaPlayer.postValue(false)
        } else {
            stopOrResumeMediaPlayer.postValue(true)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        registerReceiver(MyReceiver(), IntentFilter("notification_click"))
        return super.onStartCommand(intent, flags, startId)
    }

    private fun newNotification(text: String): Notification {
        createChannel()
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_lemon)
            .setContentTitle(resources.getString(R.string.app_name))
//            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setCustomContentView(newRemoteViews(text))
            .setCustomBigContentView(RemoteViews(packageName, R.layout.layout_notification_large))
            .build()
        return notification
    }

    private fun newRemoteViews(text: String): RemoteViews {

        remoteView = RemoteViews(packageName, R.layout.layout_notification_small).also {
            it.setTextViewText(R.id.tv_notification_name, text)
            it.setOnClickPendingIntent(
                R.id.iv_notification_next_small,
                PendingIntent.getBroadcast(
                    this,
                    0,
                    Intent("notification_click"),
                    PendingIntent.FLAG_CANCEL_CURRENT
                )
            )
        }

        return remoteView
    }


    inner class MyReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            println("=======================有人在发送广播:")
            (getSystemService(NOTIFICATION_SERVICE) as NotificationManager).notify(
                100,
                newNotification("这样子是可以的")
            )
        }
    }

    inner class MyBinder : Binder() {
        val service: MyService = this@MyService

        fun startMusic(map: HashMap<String, Any>) {
            type = ReuseListAdapter.Type.INTERNET
            currentSongListAndPosition.postValue(map)
        }

        fun startLocalMusic(map: HashMap<String, Any>) {
            type = ReuseListAdapter.Type.LOCAL
            currentSongListAndPosition.postValue(map)
        }

        fun changePosition(position: Int) {
            changeIndex(position)
        }

        fun playOrPause() {
            mediaPlayerPlayOrPause()
        }

        fun playerPause() {
            if (mediaHasResource && mediaPlayer.isPlaying) {
                mediaPlayer.pause()
            }
        }

        fun playerPlay() {
            if (mediaHasResource && !mediaPlayer.isPlaying) {
                mediaPlayer.start()
            }
        }

        fun seekTo(progress: Int) {
            mediaPlayer.seekTo(progress)
        }

        fun nextMusic() {
            next()
        }

        fun lastMusic() {
            last()
        }
    }
}