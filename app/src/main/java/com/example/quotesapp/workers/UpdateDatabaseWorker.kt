package com.example.quotesapp.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.quotesapp.utils.getQuotesRepository
import kotlinx.coroutines.coroutineScope

class UpdateDatabaseWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result = coroutineScope {
        try {
            getQuotesRepository(context = applicationContext).refreshQuotes()
            Result.success()
        } catch (ex: Exception) {
            Log.e(TAG, "Error updating database", ex)
            Result.failure()
        }
    }

    companion object {
        private const val TAG = "UpdateDatabaseWorker"
    }
}

