package com.example.graduationdesign.view.search

import android.content.Context
import android.view.View
import androidx.lifecycle.*
import com.example.graduationdesign.model.InternetModel
import com.example.graduationdesign.model.bean.search_bean.*
import com.example.graduationdesign.model.room.search.SearchHistory

class SearchViewModel : ViewModel() {

    companion object {
        private var viewModel: SearchViewModel? = null
        fun newInstance(lifecycleOwner: ViewModelStoreOwner): SearchViewModel =
            synchronized(this) {
                return@synchronized if (viewModel != null) {
                    viewModel!!
                } else {
                    viewModel =
                        ViewModelProvider(lifecycleOwner).get(SearchViewModel::class.java)
                    viewModel!!
                }
            }
    }

    private var model: InternetModel? = null

    private val _searchDefault = MutableLiveData<Data>()
    val searchDefault: LiveData<Data> = _searchDefault

    private val _searchHotList = MutableLiveData<ArrayList<HotItem>>()
    val searchHotList: LiveData<ArrayList<HotItem>> = _searchHotList

    private val _searchHistoryList = MutableLiveData<ArrayList<SearchHistory>>()
    val searchHistoryList: LiveData<ArrayList<SearchHistory>> = _searchHistoryList

    private val _searchViewCompeteList = MutableLiveData<ArrayList<SingleSuggest>>()
    val searchViewCompeteList: LiveData<ArrayList<SingleSuggest>> = _searchViewCompeteList

    private fun submitList(list: ArrayList<HotItem>) {
        _searchHotList.postValue(list)
    }

    private fun submitSuggestList(list: ArrayList<SingleSuggest>) {
        _searchViewCompeteList.postValue(list)
    }

    private fun changeSearchDefault(data: Data) {
        _searchDefault.postValue(data)
    }

    fun setInterModel(context: Context) {
        this.model = InternetModel(context)
    }

    fun getInternetModel() = this.model

    fun search(str: String?): String? {
        if (str != null) {
            return str
        } else {
            searchDefault.value?.let {
                return@let it.realkeyword
            }
            return null
        }
    }

    fun getDefaultKeyWord() {
        model?.getDefaultKeyWord({
            changeSearchDefault(it)
        }, {

        })
    }

    fun getHotListDetail() {
        model?.getHotListDetail({
            submitList(it)
        }, {

        })
    }

    fun getSearchSuggest(keyword: String) {
        if (keyword == "") return
        model?.getSearchSuggest(HashMap<String, String>().also {
            it["keywords"] = keyword
        }, {
            println("================it: $it")
            submitSuggestList(it.list)
        }, {

        })
    }

    //search
    private val _navigateBehavior = MutableLiveData<HashMap<String, Any>>()
    val navigateBehavior: LiveData<HashMap<String, Any>> = _navigateBehavior

    private val _containerHide = MutableLiveData<Int>(View.GONE)
    val containerHide: LiveData<Int> = _containerHide

    var navigateEnable = true

    fun shouldHide(visibility: Int){
        _containerHide.postValue(visibility)
    }

    fun changeNavigateBehavior(keyword: String) {
        model?.saveHistory(keyword)
        _navigateBehavior.postValue(HashMap<String, Any>().also {
            it["enable"] = navigateEnable
            it["keyword"] = keyword
        })
    }

    fun getHistoryList(){
        model?.getHistory {
            _searchHistoryList.postValue(it)
        }
    }

    fun deleteHistory(){
        model?.deleteHistory()
    }


    //search result
    private val _comprehensiveList = MutableLiveData<HashMap<String, ArrayList<*>>>()
    val comprehensiveList: LiveData<HashMap<String, ArrayList<*>>> = _comprehensiveList

    private val _viewHideAble = MutableLiveData<Boolean>()
    val viewHideAble: LiveData<Boolean> = _viewHideAble

    private val _keyword = MutableLiveData<String>()
    val keyword: LiveData<String> = _keyword

    fun changeKeyword(keyword: String) {
        if (_keyword.value == null){
            _keyword.postValue(keyword)
        }else{
            if (_keyword.value != keyword){
                _keyword.postValue(keyword)
            }
        }
    }

    private fun changeHideAble(boolean: Boolean) {
        _viewHideAble.postValue(boolean)
    }

    private fun submitToComprehensiveList(map: HashMap<String, ArrayList<*>>) {
        _comprehensiveList.postValue(map)
        changeHideAble(false)
    }

    fun getComprehensiveResultByKeyWord(type: Int) {
        this._keyword.value?.let { notNullKeyWord ->
            changeHideAble(true)
            model?.getSearchResultByKeyword(HashMap<String, String>().also {
                it["keywords"] = notNullKeyWord
                it["type"] = type.toString()
            }, { result ->
                submitToComprehensiveList(HashMap<String, ArrayList<*>>().also {
                    it["single"] = result.songs.songs
                    it["artist"] = result.artists.artists
                    it["album"] = result.albums.albums
                    it["video"] = result.videos.videos
                    it["playlist"] = result.playlists.playlists
                })
            }, {

            })
        }
    }

    //other

}