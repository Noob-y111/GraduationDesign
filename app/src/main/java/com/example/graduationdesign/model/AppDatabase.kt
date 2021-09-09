package com.example.graduationdesign.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.graduationdesign.model.bean.song_list_bean.SongBean
import com.example.graduationdesign.model.room.play.AlbumBeanConverters
import com.example.graduationdesign.model.room.play.SongBeanConverters
import com.example.graduationdesign.model.room.play.SongBeanDao
import com.example.graduationdesign.model.room.search.SearchHistory
import com.example.graduationdesign.model.room.search.SearchHistoryDao

@Database(entities = [SearchHistory::class, SongBean::class], version = 1, exportSchema = false)
@TypeConverters(SongBeanConverters::class, AlbumBeanConverters::class)
abstract class AppDatabase: RoomDatabase() {
    companion object{
        private var dbAppDatabase: AppDatabase? = null
        fun newInstance(context: Context) = dbAppDatabase ?: synchronized(this) {
            Room.databaseBuilder(context, AppDatabase::class.java, "db_fruit").build().also {
                dbAppDatabase = it
            }
        }
    }

    abstract fun historyDao(): SearchHistoryDao
    abstract fun songBeanDao(): SongBeanDao
}