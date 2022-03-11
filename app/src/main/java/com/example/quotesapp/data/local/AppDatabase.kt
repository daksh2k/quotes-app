package com.example.quotesapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.quotesapp.data.model.Quote
import com.example.quotesapp.utils.DATABASE_NAME
import com.example.quotesapp.workers.UpdateDatabaseWorker
import java.util.concurrent.TimeUnit

/**
 * The Room database for this app
 */
@Database(entities = [Quote::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun quoteDao(): QuoteDao

    companion object {

        // For Singleton instantiation
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        // Prepopulate the database from network
        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        val request = PeriodicWorkRequestBuilder<UpdateDatabaseWorker>(
                            12,
                            TimeUnit.HOURS
                        ).build()
                        WorkManager.getInstance(context).enqueue(request)
                    }
                })
                .build()
        }
    }
}