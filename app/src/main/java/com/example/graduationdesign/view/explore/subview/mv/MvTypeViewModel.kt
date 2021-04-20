package com.example.graduationdesign.view.explore.subview.mv

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.graduationdesign.model.InternetModel
import com.example.graduationdesign.model.bean.mv.MvBean

class MvTypeViewModel : ViewModel() {
    private val _mvListAndUpdateTime = MutableLiveData<HashMap<String, Any>>()
    val mvListAndUpdateTime: LiveData<HashMap<String, Any>> = _mvListAndUpdateTime

    private var model: InternetModel? = null

    fun setModel(model: InternetModel?) {
        this.model = model
    }

    private fun changeMvListAndUpdateTime(map: HashMap<String, Any>){
//        val newList = _mvListAndUpdateTime.value?.get("list") as ArrayList<*>
//        newList.addAll(map["list"] as ArrayList<Any>)
//        _mvListAndUpdateTime.postValue(HashMap<String, Any>().apply {
//            this["time"] = map["time"] as Long
//            this["list"] = newList
//        })
        _mvListAndUpdateTime.postValue(map)
    }

    fun firstRequest(type: String?) {
        type?.let { area ->
            model?.mvRankingListByArea(HashMap<String, String>().also {
                it["limit"] = "30"
                it["area"] = area
            }, {
                changeMvListAndUpdateTime(it)
            }, {

            })
        }
    }
}