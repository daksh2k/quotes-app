package com.example.quotesapp

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.quotesapp.data.DefaultQuotesRepository
import com.example.quotesapp.data.model.Quote
import com.example.quotesapp.ui.theme.pastelColors
import com.example.quotesapp.utils.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


enum class LoadingStatus { LOADING, ERROR, DONE, PRELOAD }

private const val TAG = "HOME VIEWMODEL"

class HomeViewModel(
    private val quotesRepository: DefaultQuotesRepository
) : ViewModel() {

    private var currentQuoteViewIndex by mutableStateOf(-1)

    var status by mutableStateOf(LoadingStatus.LOADING)
        private set

    private val _statusMessage = MutableStateFlow(Event(""))
    val statusMessage = _statusMessage.asStateFlow()

//    val message: LiveData<Event<String>>
//        get() = _statusMessage


    var themeColor by mutableStateOf(getRandomColor(initial = true))
        private set

    var activeTags = mutableStateListOf<String>()
        private set

//    var quotes = mutableStateListOf<Quote>()
//        private set

    private val _quotes = MutableStateFlow(mutableListOf<Quote>())
    val quotes: StateFlow<List<Quote>> = _quotes

    val currentQuoteModel: Quote?
        get() = _quotes.value.getOrNull(currentQuoteViewIndex)

    init {
        getQuotes()
    }

    /**
     * Get the quotes from the Api and set them in the ViewModel
     **/
    fun getQuotes() {
        viewModelScope.launch(Dispatchers.Main) {
            status = LoadingStatus.LOADING
            try {
                val quotesResponse = quotesRepository.getQuotes()
                _quotes.value.clear()
                _quotes.value.addAll(quotesResponse)
                currentQuoteViewIndex = 0
                status = LoadingStatus.DONE
                _statusMessage.value = Event("Fetched quotes")
            } catch (e: Exception) {
                Log.e(TAG, e.stackTraceToString())
//                statusMessage.value = Event("Error getting quotes.")
                status = LoadingStatus.ERROR
            }
        }
    }

    fun nextQuote() {
        if (_quotes.value.size - currentQuoteViewIndex < 10
            && status != LoadingStatus.PRELOAD
            && !allQuotesFetched
        ) {
            Log.d(TAG, "Preloading next quotes")
            viewModelScope.launch(Dispatchers.IO) {
                status = LoadingStatus.PRELOAD
                try {
                    val newQuotes = if (activeTags.isNotEmpty()) {
                        quotesRepository.getTaggedQuotes(tag = activeTags.joinToString(separator = " ")).quotes
                    } else {
                        quotesRepository.getQuotes()
                    }
                    newQuotes.filter { it.quoteId !in _quotes.value.map { it1 -> it1.quoteId } }
                    if (newQuotes.isEmpty()) {
                        allQuotesFetched = true
                    }
                    _quotes.value.addAll(newQuotes)
//                  QuoteApi.retrofitService.getQuotes(tag = activeTags.joinToString(separator = " "))
//                    _quotes.value.distinct()
//                    Log.d(TAG, "Preloaded!!!!!" + apiResp.quotesPresent)
                    status = LoadingStatus.DONE
                } catch (e: Exception) {
                    Log.e(TAG, e.stackTraceToString())
                }
            }
        }
        themeColor = getRandomColor()
        if (currentQuoteViewIndex < _quotes.value.size - 1) {
            currentQuoteViewIndex += 1
        } else {
            currentQuoteViewIndex = 0
        }
    }

    fun prevQuote() {
        if (currentQuoteViewIndex > 0) {
            currentQuoteViewIndex -= 1
            themeColor = getRandomColor()
        }
    }

    // TODO: Update the repository to handle tagged quotes
    fun getTaggedQuotes(tag: String) {
        var tagRemoved = false
        if (activeTags.contains(tag)) {
            activeTags.remove(tag)
            tagRemoved = true
        } else {
            if (activeTags.size > 2) {
                _statusMessage.value = Event("Cannot select more than 3 tags.")
                return
            }
            activeTags.add(tag)
        }
        viewModelScope.launch(Dispatchers.IO) {
            status = LoadingStatus.LOADING
            try {
                val apiResp =
                    quotesRepository.getTaggedQuotes(tag = activeTags.joinToString(separator = " "))
//                val apiResp = QuoteApi.retrofitService.getQuotes(tag = activeTags.joinToString(separator = " "))
                if (apiResp.quotesPresent == 0) {
                    status = LoadingStatus.ERROR
                    _statusMessage.value = Event("No quotes present for selected tags.")
                } else {
                    _quotes.value.clear()
                    _quotes.value.addAll(apiResp.quotes)
                    currentQuoteViewIndex = 0
                }
            } catch (e: Exception) {
                if (tagRemoved) activeTags.add(tag) else activeTags.remove(tag)
                _statusMessage.value =
                    Event("You need an active internet connection for getting tagged quotes.")
//                status = LoadingStatus.ERROR
            }
            status = LoadingStatus.DONE
        }
    }

    /**
     * Returns a new color every time.
     * Checks recursively with the previously returned color.
     * */
    private fun getRandomColor(initial: Boolean = false): Color {
        val randomColor = pastelColors.values.random()
        if (!initial) {
            return if (randomColor == themeColor) getRandomColor() else randomColor
        }
        return randomColor
    }

    companion object {
        private var allQuotesFetched = false
    }
}

/**
 * Factory for creating a [HomeViewModel] with a constructor that takes a
 * [DefaultQuotesRepository].
 */
class HomeViewModelFactory(
    private val repository: DefaultQuotesRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModel(repository) as T
    }
}
