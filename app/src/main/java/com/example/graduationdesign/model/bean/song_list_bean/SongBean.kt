package com.example.graduationdesign.model.bean.song_list_bean

import androidx.room.*
import com.example.graduationdesign.model.room.play.AlbumBeanConverters
import com.example.graduationdesign.model.room.play.SongBeanConverters
import com.google.gson.annotations.SerializedName

@Entity
@TypeConverters(SongBeanConverters::class, AlbumBeanConverters::class)
data class SongBean(

    @PrimaryKey
    @SerializedName("id")
    var id: String,

    @ColumnInfo(name = "song_name")
    @SerializedName("name")
    var name: String,


    @ColumnInfo(name = "artists")
    @SerializedName(value = "ar", alternate = ["artists"])
    var artists: ArrayList<ArtistBean>,

    @ColumnInfo(name = "album")
    @SerializedName(value = "al", alternate = ["album"])
    var album: AlbumBean
)
