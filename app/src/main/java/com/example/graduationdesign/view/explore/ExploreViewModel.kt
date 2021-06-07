package com.example.graduationdesign.view.explore

import androidx.lifecycle.*
import com.example.graduationdesign.model.InternetModel
import com.example.graduationdesign.model.bean.User
import com.example.graduationdesign.model.bean.mv.VideoData

class ExploreViewModel : ViewModel() {
    companion object {
        private var viewModel: ExploreViewModel? = null
        fun newInstance(lifecycleOwner: ViewModelStoreOwner): ExploreViewModel =
            synchronized(this) {
                return@synchronized if (viewModel != null) {
                    viewModel!!
                } else {
                    viewModel =
                        ViewModelProvider(lifecycleOwner).get(ExploreViewModel::class.java)
                    viewModel!!
                }
            }
    }

    private var model: InternetModel? = null

    fun setModel(model: InternetModel?){
        this.model = model
    }

    private val mvType = ArrayList<String>().apply {
        add("内地")
        add("港台")
        add("欧美")
        add("韩国")
        add("日本")
    }

    fun getMvType(position: Int) = mvType[position]
    fun getMvTypeCount() = mvType.size

    //more video
    private val _videoList = MutableLiveData<ArrayList<VideoData>>()
    val videoList: LiveData<ArrayList<VideoData>> = _videoList

    fun getVideoList(user: User?){
        user?.let { usr ->
            model?.getVideoList(HashMap<String, String>().also {
                it["cookie"] = usr.cookie!!
            }, {
                _videoList.postValue(it)
            }, {

            })
        }
    }
}