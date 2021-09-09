package com.example.graduationdesign.model.bean.club_bean

import com.example.graduationdesign.model.bean.new_album_bean.SingleMonthData

data class NewAlbumResponse(
    var code: Int,
    var albums: ArrayList<SingleMonthData>
)
