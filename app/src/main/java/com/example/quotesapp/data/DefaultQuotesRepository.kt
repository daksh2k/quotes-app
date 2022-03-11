package com.example.quotesapp.data

import android.util.Log
import com.example.quotesapp.data.local.QuoteDao
import com.example.quotesapp.data.model.Quote
import com.example.quotesapp.data.remote.QuoteApiService
import kotlinx.coroutines.*

private const val TAG = "REPOSITARY"

/**
 * Default implementation of [QuotesRepository]. Single entry point for managing tasks' data.
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
        withContext(ioDispatcher) {
            coroutineScope {
                launch { quotesLocalDataSource.deleteAll() }
            }
        }
    }

    private suspend fun updateQuotesFromRemoteDataSource() {
        withContext(ioDispatcher) {
            coroutineScope {
                launch {
                    try {
                        val remoteQuotes = quotesRemoteDataSource.getQuotes()
                        quotesLocalDataSource.deleteAll()
                        quotesLocalDataSource.insertAll(remoteQuotes.quotes)
                    } catch (ex: Exception) {
                        Log.e(TAG, "Error fetching quotes from remote.", ex)
                    }
                }
            }
        }
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
