package com.example.graduationdesign.model.room.play

import androidx.room.TypeConverter
import com.example.graduationdesign.model.bean.song_list_bean.ArtistBean
import com.example.graduationdesign.model.bean.song_list_bean.SongBean
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SongBeanConverters {

    @TypeConverter
    fun stringToObject(value: String): ArrayList<ArtistBean> {
        val listType = object : TypeToken<ArrayList<ArtistBean>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun objectToString(list: ArrayList<ArtistBean>): String {
        return Gson().toJson(list)
    }
}