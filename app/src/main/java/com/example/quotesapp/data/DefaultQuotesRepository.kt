package com.example.quotesapp.data

import android.util.Log
import com.example.quotesapp.data.local.QuoteDao
import com.example.quotesapp.data.model.Quote
import com.example.quotesapp.data.model.QuoteApiModel
import com.example.quotesapp.data.remote.QuoteApiService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

private const val TAG = "REPOSITORY"

/**
 * Default implementation of [QuotesRepository]. Single entry point for managing quotes' data.
 */
class DefaultQuotesRepository(
    private val quotesRemoteDataSource: QuoteApiService,
    private val quotesLocalDataSource: QuoteDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : QuotesRepository {

    override suspend fun getQuotes(): List<Quote> {
        try {
            updateQuotesFromRemoteDataSource()
        } catch (ex: Exception) {
            Log.e(TAG, "Error fetching quotes from remote.", ex)
        }
        return quotesLocalDataSource.getQuotes()
    }

    override suspend fun refreshQuotes() {
        updateQuotesFromRemoteDataSource()
    }

    override suspend fun deleteAllQuotes() {
        quotesLocalDataSource.deleteAll()
    }

    /**
     * Get tagged quotes. Only supported via network request.
     * @param tags List of tags
     */
    override suspend fun getTaggedQuotes(tags: List<String>): QuoteApiModel {
        return quotesRemoteDataSource.getQuotes(tag = tags.joinToString(separator = " "))
    }

    /**
     * Update quotes from network in local database.
     */
    private suspend fun updateQuotesFromRemoteDataSource() {
        val remoteQuotes = quotesRemoteDataSource.getQuotes()
        deleteAllQuotes()
        quotesLocalDataSource.insertAll(remoteQuotes.quotes)
    }

    companion object {

        // For Singleton instantiation
        @Volatile
        private var instance: DefaultQuotesRepository? = null

        fun getInstance(quoteApi: QuoteApiService, quoteDao: QuoteDao) =
            instance ?: synchronized(this) {
                instance ?: DefaultQuotesRepository(quoteApi, quoteDao).also { instance = it }
            }
    }
}
