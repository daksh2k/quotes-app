package com.example.quotesapp.data

import com.example.quotesapp.data.model.Quote

/**
 * Interface to the data layer.
 */
interface QuotesRepository {
//    fun observeQuotes(): LiveData<List<Quote>>

    suspend fun getQuotes(): List<Quote>

    suspend fun refreshQuotes()

    suspend fun deleteAllQuotes()
}