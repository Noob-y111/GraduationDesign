package com.example.graduationdesign.model.room.search

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SearchHistoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addHistory(history: SearchHistory)

    @Query("DELETE FROM SearchHistory")
    fun deleteAll()

    @Query("SELECT * FROM SearchHistory")
    fun getAllHistory(): List<SearchHistory>
}