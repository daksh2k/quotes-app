package com.example.quotesapp.data

import com.example.quotesapp.data.model.Quote
import com.example.quotesapp.data.model.QuoteApiModel

/**
 * Interface to the data layer.
 */
interface QuotesRepository {
//    fun observeQuotes(): LiveData<List<Quote>>

    suspend fun getQuotes(): List<Quote>

    suspend fun getTaggedQuotes(tags: List<String>): QuoteApiModel

    suspend fun refreshQuotes()

    suspend fun deleteAllQuotes()
}