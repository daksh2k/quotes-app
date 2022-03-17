package com.example.quotesapp.data

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.quotesapp.data.local.QuoteDao
import com.example.quotesapp.data.model.Quote
import com.example.quotesapp.data.model.QuoteApiModel
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
    override fun observeQuotes(): LiveData<List<Quote>> {
        return quotesLocalDataSource.observeQuotes()
    }

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

    override suspend fun getTaggedQuotes(tag: String): QuoteApiModel {
        return quotesRemoteDataSource.getQuotes(tag = tag)
    }

    private suspend fun updateQuotesFromRemoteDataSource() {
//        withContext(ioDispatcher) {
//            coroutineScope {
//                launch {
//                    try {
        val remoteQuotes = quotesRemoteDataSource.getQuotes()
        deleteAllQuotes()
        quotesLocalDataSource.insertAll(remoteQuotes.quotes)
//                    } catch (ex: Exception) {
//                        Log.e(TAG, "Error fetching quotes from remote.", ex)
//                    }
//                }
//            }
//        }
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
