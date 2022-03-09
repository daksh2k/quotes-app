package com.example.quotesapp.home

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quotesapp.network.Quote
import com.example.quotesapp.network.QuoteApi
import com.example.quotesapp.ui.theme.pastelColors
import kotlinx.coroutines.launch


enum class QuoteApiStatus { LOADING, ERROR, DONE, PRELOAD }

private const val TAG = "LOADING QUOTES"

class HomeViewModel : ViewModel() {

    private var currentQuoteViewIndex by mutableStateOf(-1)

    var status by mutableStateOf(QuoteApiStatus.LOADING)
        private set

    var themeColor by mutableStateOf(getRandomColor(initial = true))
        private set

    var activeTags = mutableStateListOf<String>()
        private set

    var quotes = mutableStateListOf<Quote>()
        private set

    val currentQuote: Quote?
        get() = quotes.getOrNull(currentQuoteViewIndex)

    init {
        getQuotes()
    }

    /*
    * Get the quotes from the Api and set them in the viewmodel*/
    private fun getQuotes() {
        viewModelScope.launch {
            status = QuoteApiStatus.LOADING
            try {
                val apiResp = QuoteApi.retrofitService.getQuotes()
                quotes.addAll(apiResp.quotes)
                currentQuoteViewIndex = 0
                status = QuoteApiStatus.DONE
            } catch (e: Exception) {
                status = QuoteApiStatus.ERROR
            }
        }
    }

    fun nextQuote() {
        if (quotes.size - currentQuoteViewIndex < 10 && status != QuoteApiStatus.PRELOAD) {
            Log.d(TAG, "Preloading next quotes")
            viewModelScope.launch {
                status = QuoteApiStatus.PRELOAD
                try {
                    val apiResp = QuoteApi.retrofitService.getQuotes()
                    quotes.addAll(apiResp.quotes)
                    quotes.distinct()
                    Log.d(TAG, "Preloaded!!!!!" + apiResp.quotesPresent)
                    status = QuoteApiStatus.DONE
                } catch (e: Exception) {
                    Log.d(TAG, e.toString())
                }
            }
        }
        if (currentQuoteViewIndex < quotes.size) {
            currentQuoteViewIndex += 1
            themeColor = getRandomColor()
        }
    }

    fun prevQuote() {
        if (currentQuoteViewIndex > 0) {
            currentQuoteViewIndex -= 1
            themeColor = getRandomColor()
        }
    }

    fun getTaggedQuotes(tag: String) {
        if (activeTags.contains(tag)) {
            activeTags.remove(tag)
        } else {
            if (activeTags.size > 2) {
                return
            }
            activeTags.add(tag)
        }
        viewModelScope.launch {
            status = QuoteApiStatus.LOADING
            try {
                val apiResp =
                    QuoteApi.retrofitService.getTaggedQuotes(tag = activeTags.joinToString(separator = " "))
                quotes.clear()
                quotes.addAll(apiResp.quotes)
                currentQuoteViewIndex = 0
                status = QuoteApiStatus.DONE
            } catch (e: Exception) {
                status = QuoteApiStatus.ERROR
            }
        }
    }

    private fun getRandomColor(initial: Boolean = false): Color {
        val randomColor = pastelColors.values.random()
        if (!initial) {
            return if (randomColor == themeColor) getRandomColor() else randomColor
        }
        return randomColor
    }
}