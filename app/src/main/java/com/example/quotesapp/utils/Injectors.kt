package com.example.quotesapp.utils

import android.content.Context
import com.example.quotesapp.HomeViewModelFactory
import com.example.quotesapp.data.DefaultQuotesRepository
import com.example.quotesapp.data.QuotesRepository
import com.example.quotesapp.data.local.AppDatabase
import com.example.quotesapp.data.remote.QuoteApi

fun getQuotesRepository(context: Context): QuotesRepository {
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