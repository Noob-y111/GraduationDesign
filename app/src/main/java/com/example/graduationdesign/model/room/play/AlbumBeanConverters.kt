package com.example.graduationdesign.model.room.play

import androidx.room.TypeConverter
import com.example.graduationdesign.model.bean.song_list_bean.AlbumBean
import com.google.gson.Gson

class AlbumBeanConverters {

    @TypeConverter
    fun stringToObject(value: String): AlbumBean{
        return Gson().fromJson(value, AlbumBean::class.java)
    }

    @TypeConverter
    fun objectToString(albumBean: AlbumBean): String{
        return Gson().toJson(albumBean)
    }
}