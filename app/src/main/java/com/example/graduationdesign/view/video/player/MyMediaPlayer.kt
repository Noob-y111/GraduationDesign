package com.example.graduationdesign.view.video.player

import android.media.MediaPlayer
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

class MyMediaPlayer : MediaPlayer(), LifecycleObserver {
    private var enable = false

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun mediaResume() {
        if (enable)
            start()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun mediaPause() {
        if (isPlaying)
            pause()
    }

    fun setDataSource(path: String?, enable: Boolean) {
        setDataSource(path)
        this.enable = enable
    }
}