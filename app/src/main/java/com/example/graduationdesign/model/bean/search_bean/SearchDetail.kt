package com.example.graduationdesign.model.bean.search_bean

import com.google.gson.annotations.SerializedName

data class SearchDetail(
    var code: Int,
    @SerializedName("data")
    var data: Data
)

data class Data(
    var showKeyword: String,
    var realkeyword: String,
)

data class HotDetail(
    var code: Int,
    var data: ArrayList<HotItem>
)

data class HotItem(
    var searchWord:String,
    var score: Int,
    var content: String,
    var iconUrl: String?,
)

data class SuggestResult(
    var code: Int,
    var result: Result
)

data class Result(
    @SerializedName("allMatch")
    var list: ArrayList<SingleSuggest>
)

data class SingleSuggest(
    var keyword: String
)
