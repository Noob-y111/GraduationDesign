package com.example.graduationdesign.model.bean.song_list_bean

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class AlbumBean(

    @PrimaryKey
    @SerializedName("id")
    var id: String,

    @ColumnInfo(name = "name")
    @SerializedName("name")
    var name: String,

    @ColumnInfo(name = "image")
    @SerializedName("picUrl", )
    var picUrl: Any?

)
