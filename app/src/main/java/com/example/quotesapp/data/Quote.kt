package com.example.quotesapp.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quotes")
data class Quote(
    @PrimaryKey @ColumnInfo(name = "id") val quoteId: String,
    val author: String,
    val quote: String,
    val source: String,
    val tags: String,
)