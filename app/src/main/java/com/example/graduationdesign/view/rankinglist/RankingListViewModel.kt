package com.example.graduationdesign.view.rankinglist

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.graduationdesign.model.InternetModel
import com.example.graduationdesign.model.bean.ranking_list_bean.ListDetail

class RankingListViewModel : ViewModel() {
    private var model: InternetModel? = null

    fun initInternetModel(context: Context) {
        model = InternetModel(context)
    }

    private val _rankingList = MutableLiveData<HashMap<String, ArrayList<ListDetail>>>()
    val rankingList: LiveData<HashMap<String, ArrayList<ListDetail>>> = _rankingList

    private fun showRankingItem(map: HashMap<String, ArrayList<ListDetail>>) {
        _rankingList.postValue(map)
    }

    fun getRankingList() {
        model?.getRankingDetail(
            { official: ArrayList<ListDetail>, other: ArrayList<ListDetail> ->
                showRankingItem(HashMap<String, ArrayList<ListDetail>>().also {
                    it["official"] = official
                    it["other"] = other
                })
            }, {

            })
    }
}