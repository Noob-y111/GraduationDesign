package com.example.graduationdesign.model.room.search

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SearchHistory(

    @PrimaryKey
    var keyWord: String
)
