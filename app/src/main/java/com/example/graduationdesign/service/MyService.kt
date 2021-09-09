package com.example.graduationdesign.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.media.MediaPlayer
import android.net.Uri
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import com.android.volley.VolleyError
import com.bumptech.glide.Glide
import com.example.graduationdesign.R
import com.example.graduationdesign.callback.BaseBehaviorCallBack
import com.example.graduationdesign.model.InternetModel
import com.example.graduationdesign.model.bean.song_list_bean.ArtistBean
import com.example.graduationdesign.model.bean.song_list_bean.SongBean
import com.example.graduationdesign.view.main.MainActivity
import com.example.graduationdesign.view.reuse.adapter.ReuseListAdapter
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.concurrent.thread

const val POSITION = "position"
const val SONG_LIST = "songList"

class MyService : LifecycleService() {

    companion object {
        private const val CHANNEL_ID = "my player channel id"
        private const val NEXT = "NEXT"
        private const val LAST = "LAST"
        private const val PLAY_PAUSE = "PLAY_PAUSE"
        private const val NOTIFICATION_CLICK = "notification_click"
        private const val BEHAVIOR = "behavior"
    }

    private val intentNext = Intent(NEXT)
    private val intentLast = Intent(LAST)
    private val intentPlayPause = Intent(PLAY_PAUSE)
    private val subScript = SubScript()

    private lateinit var intentToMain: Intent

    private var channel: NotificationChannel? = null
    private lateinit var mediaPlayer: MediaPlayer
    private val model = InternetModel(this)
    val currentSongListAndPosition = MutableLiveData<HashMap<String, Any>>()
    val progressBarDuration = MutableLiveData<Int>(0)
    val progressBarPosition = MutableLiveData<Int>(0)
    val canFooterShow = MutableLiveData(false)
    var progressBarBuffer = MutableLiveData<Int>(0)
    val stopOrResumeMediaPlayer = MutableLiveData<Boolean>()
    private var type = ReuseListAdapter.Type.NONE
    private val receiver = MyReceiver()

    private lateinit var playOrder: String


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
        intentToMain = Intent(this, MainActivity::class.java)
//        executeForeground()
        playOrder = readOrder()
        mediaPlayer = MediaPlayer().also { media ->
            media.setOnCompletionListener {
                cancelTimerTask()
                it.reset()
                mediaHasResource = false
                updateMediaPlayerState()
                nextBehavior()
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

    private fun nextBehavior(){
        when(playOrder){
            SubScript.RANDOM_NEXT -> randomNext()
            SubScript.LOOP_NEXT -> looping()
            else -> next()
        }
    }

    private fun readOrder(): String{
        val sharedPreferences = getSharedPreferences("play_order", Context.MODE_PRIVATE)
        return sharedPreferences.getString(SubScript.PLAY_ORDER, SubScript.NORMAL_NEXT)!!
    }

    private fun getMediaPlayerCurrentPosition() = mediaPlayer.currentPosition
    private fun getMediaPlayerDuration() = mediaPlayer.duration

    private fun setProgressDuration() {
        progressBarDuration.postValue(getMediaPlayerDuration())
    }

    private fun newNotification(song: SongBean, bitmap: Bitmap, playPause: Boolean): Notification {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_lemon)
            .setContentTitle(resources.getString(R.string.app_name))
            .setOngoing(true)
            .setCustomContentView(newSmallRemoteViews(song, bitmap, playPause))
            .setCustomBigContentView(newLargeRemoteViews(song, bitmap, playPause))
            .build()
    }

    inner class MyReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.let {
                println("================it.action: ${it.action}")
                when (it.action) {
                    NEXT -> next()
                    LAST -> last()
                    PLAY_PAUSE -> mediaPlayerPlayOrPause()
                }
            }
        }
    }

    private fun concatArtists(list: ArrayList<ArtistBean>): String {
        var artists = ""
        list.forEach {
            artists += it.name
            if (list.lastIndex != list.indexOf(it)) {
                artists += "/"
            }
        }
        return artists
    }

    private fun newLargeRemoteViews(
        song: SongBean,
        bitmap: Bitmap,
        playPause: Boolean
    ): RemoteViews {
        return RemoteViews(packageName, R.layout.layout_notification_large).also {
            it.setTextViewText(R.id.tv_notification_name_large, song.name)
            it.setImageViewBitmap(R.id.iv_notification_image_large, bitmap)
            it.setTextViewText(R.id.tv_notification_artist_large, concatArtists(song.artists))

            if (playPause) {
                it.setImageViewResource(R.id.iv_notification_play_pause_large, R.drawable.play)
            } else {
                it.setImageViewResource(R.id.iv_notification_play_pause_large, R.drawable.pause)
            }

            it.setOnClickPendingIntent(
                R.id.iv_notification_next_large,
                PendingIntent.getBroadcast(
                    this,
                    0,
                    intentNext,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
            )

            it.setOnClickPendingIntent(
                R.id.iv_notification_last_large,
                PendingIntent.getBroadcast(
                    this,
                    0,
                    intentLast,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
            )

            it.setOnClickPendingIntent(
                R.id.iv_notification_play_pause_large,
                PendingIntent.getBroadcast(
                    this,
                    0,
                    intentPlayPause,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
            )

            it.setOnClickPendingIntent(
                R.id.iv_notification_image_large,
                PendingIntent.getActivity(this, 0, intentToMain, PendingIntent.FLAG_UPDATE_CURRENT)
            )
        }
    }

    private fun newSmallRemoteViews(
        song: SongBean,
        bitmap: Bitmap,
        playPause: Boolean
    ): RemoteViews {
        return RemoteViews(packageName, R.layout.layout_notification_small).also {
            it.setTextViewText(R.id.tv_notification_name, song.name)
            it.setImageViewBitmap(R.id.iv_notification_image_small, bitmap)

            if (playPause) {
                it.setImageViewResource(R.id.iv_notification_play_pause_small, R.drawable.play)
            } else {
                it.setImageViewResource(R.id.iv_notification_play_pause_small, R.drawable.pause)
            }

            it.setOnClickPendingIntent(
                R.id.iv_notification_next_small,
                PendingIntent.getBroadcast(
                    this,
                    0,
                    intentNext,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
            )

            it.setOnClickPendingIntent(
                R.id.iv_notification_image_small,
                PendingIntent.getActivity(this, 0, intentToMain, PendingIntent.FLAG_UPDATE_CURRENT)
            )

            it.setOnClickPendingIntent(
                R.id.iv_notification_last_small,
                PendingIntent.getBroadcast(
                    this,
                    0,
                    intentLast,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
            )

            it.setOnClickPendingIntent(
                R.id.iv_notification_play_pause_small,
                PendingIntent.getBroadcast(
                    this,
                    0,
                    intentPlayPause,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
            )
        }
    }

    private fun sendNotification(song: SongBean, playPause: Boolean) {
        createChannel()
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        thread {
            try {
                val bitmap: Bitmap = Glide.with(this).asBitmap().error(R.drawable.shimmer_bg).load(song.album.picUrl).submit().get()
                MainScope().launch {
                    notificationManager.notify(1, newNotification(song, bitmap, playPause))
                }
            }catch (e: Exception){

            }
        }
    }

    private fun executeForeground() {
        createChannel()
        val remoteView = RemoteViews(packageName, R.layout.layout_notification_small).also {
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
//        startForeground(1, newNotification("我先试试"))
    }

    private fun createChannel() {
        if (channel == null) {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
                val notificationManager =
                    getSystemService(NOTIFICATION_SERVICE) as NotificationManager
                if (notificationManager.notificationChannels.size != 0) {
                    return
                }
                val name = "this is my player channel name"
                val importance = NotificationManager.IMPORTANCE_HIGH
                channel = NotificationChannel(CHANNEL_ID, name, importance)
                notificationManager.createNotificationChannel(channel!!)
            }
        }
    }

    private fun liveDataObserve() {
        currentSongListAndPosition.observe(this, {
            canFooterShow.postValue(true)
            val position = it[POSITION] as Int
            val list = it[SONG_LIST] as ArrayList<*>
            val song: SongBean = list[position] as SongBean
            when (type) {
                ReuseListAdapter.Type.LOCAL -> {
                    startLocalMusicById(song.id)
                }

                ReuseListAdapter.Type.INTERNET -> {
                    model.savePlayHistory(song)
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
                else -> {
                }
            }
        })

        stopOrResumeMediaPlayer.observe(this, { boolean ->
            currentSongListAndPosition.value?.let {
                val position = it[POSITION] as Int
                val list = it[SONG_LIST] as ArrayList<*>
                val song = list[position] as SongBean
                sendNotification(song, boolean)
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

            }
        })
    }

    private fun startMusicByStringUrl(path: String) {
        cancelTimerTask()
        mediaPlayer.reset()
        mediaPlayer.setDataSource(path)
        mediaPlayer.prepareAsync()
    }

    private fun startLocalMusicById(id: String) {
        cancelTimerTask()
        mediaPlayer.reset()
        mediaPlayer.setDataSource(
            application,
            Uri.parse("content://media/external/audio/media/$id")
        )
        mediaPlayer.prepareAsync()
    }

    private fun next() {
        currentSongListAndPosition.value?.let {
            val position = it[POSITION] as Int
            val size = (it[SONG_LIST] as ArrayList<*>).size
            changeIndex(subScript.normalNext(position, size))
        }
    }

    private fun last() {
        currentSongListAndPosition.value?.let {
            val position = it[POSITION] as Int
            val size = (it[SONG_LIST] as ArrayList<*>).size
            changeIndex(subScript.normalLast(position, size))
        }
    }

    private fun looping(){
        currentSongListAndPosition.value?.let {
            val position = it[POSITION] as Int
            currentSongListAndPosition.value?.set(POSITION, position)
            currentSongListAndPosition.value = currentSongListAndPosition.value
        }
    }

    private fun randomNext() {
        currentSongListAndPosition.value?.let {
//            val position = it[POSITION] as Int
            val size = (it[SONG_LIST] as ArrayList<*>).size
            changeIndex(subScript.randomNext(size))
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
        cancelTimerTask()
        mediaPlayer.release()
        unregisterReceiver(receiver)
        (getSystemService(NOTIFICATION_SERVICE) as NotificationManager).cancelAll()
        stopSelf()
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

    private fun mRegisterReceiver() {
        val intentFilter = IntentFilter()
        intentFilter.addAction(NEXT)
        intentFilter.addAction(LAST)
        intentFilter.addAction(PLAY_PAUSE)
        registerReceiver(receiver, intentFilter)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        mRegisterReceiver()
        return super.onStartCommand(intent, flags, startId)
    }

    private fun savePlayOrder(order: String){
        playOrder = order
        getSharedPreferences("play_order", MODE_PRIVATE).edit().also {
            it.putString(SubScript.PLAY_ORDER, order)
            it.apply()
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

        fun saveOrder(order: String){
            savePlayOrder(order)
        }

        fun getOrder(): String{
            return playOrder
        }

        fun changePosition(position: Int) {
            changeIndex(position)
        }

        fun playOrPause() {
            mediaPlayerPlayOrPause()
        }

        fun playerIsPlaying(): Boolean{
            return if (mediaHasResource){
                mediaPlayer.isPlaying
            }else{
                false
            }
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