package com.example.graduationdesign.view.club

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.viewpager2.widget.ViewPager2
import com.example.graduationdesign.model.InternetModel
import com.example.graduationdesign.model.bean.ImageOfBanner
import com.example.graduationdesign.tools.JudgeVolleyError
import com.example.imitationqqmusic.model.tools.DpPxUtils
import java.util.*
import kotlin.collections.ArrayList

class MusicClubViewModel : ViewModel() {

//    private  var model: InternetModel? = null
//
//    private val _currentBannerIndex = MutableLiveData<Int>()
//    val currentBannerIndex: LiveData<Int> = _currentBannerIndex
//
//    private val _currentBannerIndexWithAnimation = MutableLiveData<Int>()
//    val currentBannerIndexWithAnimation: LiveData<Int> = _currentBannerIndexWithAnimation
//
//    private var position: Int = -1
//
//    private var timer = Timer()
//    private var timerTask = object : TimerTask(){
//        override fun run() {
//            position = (position + 1) % (_bannerImageList.value?.size?.plus(2)!!)
//            _currentBannerIndexWithAnimation.postValue(position)
//        }
//    }
//
//    fun cancelTimer(){
//        timer?.cancel()
//        timerTask?.cancel()
//    }
//
//    fun setDataModel(context: Context){
//        model = InternetModel(context)
//    }
//
//    fun startScroll(){
//        timer.schedule(timerTask, 5000, 5000)
//    }
//
//    fun getScaleHeight(width: Int): Float {
//        return (420f / 1080f) * width
//    }
//
//    fun dragEnded(state: Int){
//        if (state == 0){
//            judgeBannerIndexIsEndOrStart(this.position)
//        }
//    }
//
//    fun setPosition(position: Int){
//        this.position = position
//    }
//
////    private fun judgeBannerIndex(position: Int): Int{
////        if (position == -1 ) return -1
////        val count = _bannerImageList.value?.size?.plus(2)
////        println("================position: $position")
////        return if (count != null) {
////            when (position) {
////                0 -> count - 2
////                count - 1 -> 1
////                else -> position
////            }
////        } else{
////            -1
////        }
////    }
//
//    private fun judgeBannerIndexIsEndOrStart(position: Int) {
//        if (position == -1 ) return
//        val count = _bannerImageList.value?.size?.plus(2)
//        println("================position: $position")
//        if (count != null) {
//            when (position) {
//                0 -> _currentBannerIndex.postValue(count - 2)
//                count - 1 -> _currentBannerIndex.postValue(1)
//            }
//        }
//    }
//
//    fun getTestList(){
//        _bannerImageList.postValue(ArrayList<ImageOfBanner>().also {
//            it.add(ImageOfBanner("http://p1.music.126.net/KDD-VY7mCz-VRI83VHW6kA==/109951165840043101.jpg"))
//            it.add(ImageOfBanner("http://p1.music.126.net/-ItirBQmXa7iGB_fov3i7A==/109951165840625600.jpg"))
//            it.add(ImageOfBanner("http://p1.music.126.net/0dHl0ciJw-8FfkIZQZLltg==/109951165840467848.jpg"))
//            it.add(ImageOfBanner("http://p1.music.126.net/TShMxRgXpTDjbRTMgCaT0g==/109951165840502226.jpg"))
//            it.add(ImageOfBanner("http://p1.music.126.net/KjZNRMkV0oKzdS1dBFkFKg==/109951165840045824.jpg"))
//        })
//    }
//
//    fun getBannerList() {
//            model?.getBannerList({
//                _bannerImageList.postValue(it)
//            }, {
//                println("=======================失败:")
//                val str = JudgeVolleyError.judgeVolleyError(it)
//                println("================str: $str")
//            })
//    }
}