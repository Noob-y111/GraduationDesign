package com.example.graduationdesign.model.room.play

import androidx.room.*
import com.example.graduationdesign.model.bean.song_list_bean.SongBean

@Dao
interface SongBeanDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(songBean: SongBean)

    @Delete
    fun delete(songBean: SongBean)

    @Query("SELECT * FROM SongBean")
    fun queryList(): List<SongBean>

    @Query("DELETE FROM SongBean")
    fun deleteAll()
}