package com.example.quotesapp.data.model

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