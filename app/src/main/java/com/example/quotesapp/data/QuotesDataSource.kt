package com.example.quotesapp.data

import androidx.lifecycle.LiveData
import com.example.quotesapp.data.model.Quote

/**
 * Main entry point for accessing quotes data.
 */
interface QuotesDataSource {

    fun observeQuotes(): LiveData<List<Quote>>

    suspend fun getQuotes(forceUpdate: Boolean = false): List<Quote>

    suspend fun refreshQuotes()

    suspend fun deleteAllQuotes()
}