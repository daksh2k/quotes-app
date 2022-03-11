package com.example.quotesapp.network

data class QuoteApiModel(
    val ignoredAuthors: String?,
    val ignoredTags: String?,
    val isRandom: Boolean,
    val quotes: List<QuoteModel>,
    val quotesPresent: Int,
    val quotesReturned: Int,
    val requestedAuthors: String?,
    val requestedTags: String?
)

data class QuoteModel(
    val quoteId: String,
    val author: String,
    val quote: String,
    val source: String,
    val tags: String,
)