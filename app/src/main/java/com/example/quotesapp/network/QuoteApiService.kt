package com.example.quotesapp.network

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://smile-plz.smileplz.repl.co"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()


interface QuoteApiService{
    @GET("/quotes?format=json")
    suspend fun getQuotes(@Query("length") length: String = "20,150",
    ): QuoteApiModel

    @GET("/quotes?format=json")
    suspend fun getTaggedQuotes(@Query("tags") tags: String
    ): QuoteApiModel
}

object QuoteApi{
    val retrofitService: QuoteApiService by lazy {
        retrofit.create(QuoteApiService::class.java)
    }
}