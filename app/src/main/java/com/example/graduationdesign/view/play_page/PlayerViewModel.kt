package com.example.graduationdesign.view.play_page

import android.graphics.Bitmap
import androidx.lifecycle.*
import com.example.graduationdesign.model.InternetModel
import com.example.graduationdesign.model.bean.lrc.LyricBean
import com.example.graduationdesign.model.bean.lrc.StringLrc
import kotlinx.coroutines.launch

class PlayerViewModel : ViewModel() {

    companion object {
        private var viewModel: PlayerViewModel? = null
        fun newInstance(lifecycleOwner: ViewModelStoreOwner): PlayerViewModel = synchronized(this) {
            return@synchronized if (viewModel != null) {
                viewModel!!
            } else {
                viewModel = ViewModelProvider(lifecycleOwner).get(PlayerViewModel::class.java)
                viewModel!!
            }
        }
    }

    private var model: InternetModel? = null

    fun setModel(model: InternetModel?) {
        this.model = model
    }

    private var _imageBitmap = MutableLiveData<Bitmap>()
    val imageBitmap: LiveData<Bitmap> = _imageBitmap

    fun changeImageBitmap(bitmap: Bitmap) {
        _imageBitmap.postValue(bitmap)
    }

    //lyric
    private val _lyricList = MutableLiveData<ArrayList<LyricBean>>()
    val lyricList: LiveData<ArrayList<LyricBean>> = _lyricList

    private val _currentLyricTime = MutableLiveData<Int>()
    val currentLyricTime: LiveData<Int> = _currentLyricTime

    private val _hasLyric = MutableLiveData<Boolean>()
    val hasLyric: LiveData<Boolean> = _hasLyric

    fun changeTime(time: Int) {
        _currentLyricTime.postValue(time)
    }

    fun getLyricById(sid: String) {
        _lyricList.postValue(ArrayList())
        model?.getLyric(HashMap<String, String>().also {
            it["id"] = sid
        }, {
            lyricFormat(it)
        }, {

        })
    }

    private fun lyricFormat(lyric: StringLrc?) {
        lyric?.let {
            viewModelScope.launch {
                try {
                    val lyricList = ArrayList<LyricBean>()
                    val lyricWithTime = lyric.lrcStr.split("\n")
                    for (i in lyricWithTime) {

                        val tempLyric = i.substring(i.indexOf("]") + 1)
                        if (tempLyric == "") continue
                        val min = i.substring(i.indexOf("[") + 1, i.indexOf(":"))
                        val sec = i.substring(i.indexOf(":") + 1, i.indexOf("."))
                        val mills = i.substring(i.indexOf(".") + 1, i.indexOf("]"))

                        val startTime = timeToDuration(min, sec, mills)
                        lyricList.add(LyricBean(lrc = tempLyric, start = startTime, end = null))
                    }

                    for (i in 1 until lyricList.size) {
                        lyricList[i - 1].end = lyricList[i].start
                        if (i == lyricList.size - 1) {
                            lyricList[i].end = lyricList[i].start + 100
                        }
                    }

                    _lyricList.postValue(lyricList)
                    _hasLyric.postValue(true)
                }catch (e: Exception){
                    _hasLyric.postValue(false)
                }

            }
        }

        lyric ?: kotlin.run {
            _hasLyric.postValue(false)
        }
    }

    private fun timeToDuration(min: String, sec: String, mills: String) =
        min.toInt() * 60 * 1000 + sec.toInt() * 1000 + mills.toInt()

}
