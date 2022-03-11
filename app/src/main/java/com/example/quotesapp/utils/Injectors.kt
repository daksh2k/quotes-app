package com.example.quotesapp.utils

import android.content.Context
import com.example.quotesapp.HomeViewModelFactory
import com.example.quotesapp.data.DefaultQuotesRepository
import com.example.quotesapp.data.local.AppDatabase
import com.example.quotesapp.data.remote.QuoteApi

private fun getQuotesRepository(context: Context): DefaultQuotesRepository {
    return DefaultQuotesRepository.getInstance(
        QuoteApi.retrofitService,
        AppDatabase.getInstance(context.applicationContext).quoteDao()
    )
}

fun provideHomeViewModelFactory(
    context: Context
): HomeViewModelFactory {
    return HomeViewModelFactory(getQuotesRepository(context))
}