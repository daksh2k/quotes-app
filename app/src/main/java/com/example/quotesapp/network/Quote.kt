package com.example.quotesapp.network

data class QuoteApiModel(
    val ignoredAuthors: String?,
    val ignoredTags: String?,
    val isRandom: Boolean,
    val quotes: List<Quote>,
    val quotesPresent: Int,
    val quotesReturned: Int,
    val requestedAuthors: String?,
    val requestedTags: String?
)

data class Quote(
    val author: String,
    val quote: String,
    val source: String,
    val tags: String,
)