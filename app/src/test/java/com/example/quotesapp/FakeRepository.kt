package com.example.quotesapp

import com.example.quotesapp.data.QuotesRepository
import com.example.quotesapp.data.model.Quote
import com.example.quotesapp.data.model.QuoteApiModel

class FakeRepository : QuotesRepository {

    private val quotesData = listOf(
        Quote(
            quoteId = "1234",
            author = "Albert Einstein",
            quote = "The only reason for time is so that everything doesn't happen at once",
            source = "",
            tags = "live, laugh, love, happy"
        ),
        Quote(
            quoteId = "5678",
            author = "George R.R. Martin",
            quote = "Before he had lost his sight",
            source = "",
            tags = "qwerty, random, tag"
        )
    )

    private val taggedQuotes = QuoteApiModel(
        quotes = quotesData,
        quotesReturned = 2,
        quotesPresent = 100,
        ignoredAuthors = null,
        ignoredTags = null,
        isRandom = true,
        requestedAuthors = null,
        requestedTags = null
    )

    override suspend fun getQuotes(): List<Quote> {
        return quotesData
    }

    override suspend fun getTaggedQuotes(tags: List<String>): QuoteApiModel {
        return taggedQuotes
    }

    override suspend fun refreshQuotes() {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAllQuotes() {
        TODO("Not yet implemented")
    }

}