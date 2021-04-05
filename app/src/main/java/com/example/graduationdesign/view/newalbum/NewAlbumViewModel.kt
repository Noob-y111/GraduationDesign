package com.example.graduationdesign.view.newalbum

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.example.graduationdesign.model.InternetModel
import com.example.graduationdesign.model.bean.new_album_bean.SingleMonthData
import com.example.graduationdesign.view.newalbum.dapter.AlbumAdapter
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class NewAlbumViewModel : ViewModel() {
    private var model: InternetModel? = null
    private var month: Int
    private var year: Int
    private val calendar = Calendar.getInstance()

    var isLoading = false

    init {
        month = calendar.get(Calendar.MONTH) + 1
        year = calendar.get(Calendar.YEAR)
    }

    private fun resetDate() {
        month = calendar.get(Calendar.MONTH) + 1
        year = calendar.get(Calendar.YEAR)
    }

    fun initInternetModel(context: Context) {
        model = InternetModel(context)
    }

    private val _list = MutableLiveData<ArrayList<SingleMonthData>>().also {
        it.postValue(ArrayList())
    }
    val list: LiveData<ArrayList<SingleMonthData>> = _list

    private val _footerState = MutableLiveData<AlbumAdapter.FooterState>()
    val footerState: LiveData<AlbumAdapter.FooterState> = _footerState

    override fun onCleared() {
        super.onCleared()
        resetDate()
    }

    private fun showList(list: ArrayList<SingleMonthData>) {
        val newList = _list.value
        newList!!.addAll(list)
        _list.postValue(newList)
    }

    private fun changeState(state: AlbumAdapter.FooterState) {
        _footerState.postValue(state)
    }

    private fun putParameters(): HashMap<String, String> {
        HashMap<String, String>().also {
            it["year"] = year.toString()
            it["month"] = month.toString()
            return it.also { map ->
                println("================map: $map")
            }
        }
    }

    private fun decreaseMonth() {
        if (month - 1 <= 0) {
            month = 12
            year -= 1
        } else {
            month -= 1
        }
    }

    fun firstRequest() {
        getTheNewDiscShelves(putParameters())
    }

    private fun requestData() {
        getTheNewDiscShelves(putParameters())
    }

    fun pagingLoading(loadEnable: Boolean, state: Int) {
        if (isLoading) return else isLoading = true
        if (loadEnable) {
            if (state == RecyclerView.SCROLL_STATE_IDLE) {
                changeState(AlbumAdapter.FooterState.LOADING)
                requestData()
            }
        }
    }

    private fun getTheNewDiscShelves(map: HashMap<String, String>) {
        model?.getTheNewDiscShelves(map, {
            println("================it: $it")
            showList(it.monthData)
            if (!it.hasMore) {
                decreaseMonth()
            }
        }, {

        })
    }
}