package com.example.quotesapp.data.remote

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


@ExperimentalCoroutinesApi
class QuoteApiTest {

    private lateinit var server: MockWebServer

    private lateinit var retrofit: QuoteApiService

    @Before
    fun setUp() {
        server = MockWebServer()
        server.start()
        server.url("/")
        retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(server.url("/").toString())
            .build()
            .create(QuoteApiService::class.java)
    }

    @Test
    fun testQuotesMethod() {
        val mockedResponse = MockResponse()
        mockedResponse.setResponseCode(200)
        mockedResponse.setBody("{}") // sample JSON
        server.enqueue(mockedResponse)

        runBlocking {
            val apiResp = retrofit.getQuotes()
            println(apiResp)
            assertThat(apiResp).isNotNull()
        }


    }

    @After
    fun closeServer() {
        server.shutdown()
    }
}

