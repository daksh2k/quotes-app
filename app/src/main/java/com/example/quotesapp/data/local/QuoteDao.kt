package com.example.quotesapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.quotesapp.data.model.Quote

@Dao
interface QuoteDao {
    @Query("SELECT * FROM quotes")
    suspend fun getQuotes(): List<Quote>

    @Query("DELETE FROM quotes")
    suspend fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(quotes: List<Quote>)
}