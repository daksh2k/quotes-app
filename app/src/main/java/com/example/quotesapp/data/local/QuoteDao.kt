package com.example.quotesapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.quotesapp.data.model.Quote

@Dao
interface QuoteDao {
    /**
     * Get all quotes from the table
     *
     * @return all quotes
     */
    @Query("SELECT * FROM quotes")
    suspend fun getQuotes(): List<Quote>

    /**
     * Observes list of quotes.
     *
     * @return all quotes.
     */
//    @Query("SELECT * FROM quotes")
//    fun observeQuotes(): LiveData<List<Quote>>

    /**
     * Delete all quotes.
     */
    @Query("DELETE FROM quotes")
    suspend fun deleteAll()

    /**
     * Insert the quotes in the db
     *
     * @param quotes List of quotes to insert
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(quotes: List<Quote>)
}