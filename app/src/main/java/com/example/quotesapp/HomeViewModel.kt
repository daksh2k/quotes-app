package com.example.quotesapp

import android.util.Log
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
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


enum class LoadingStatus { LOADING, ERROR, DONE, PRELOAD }

class HomeViewModel(
    private val quotesRepository: DefaultQuotesRepository
) : ViewModel() {

    private val _currentQuoteViewIndex = MutableStateFlow(-1)

    private val _status = MutableStateFlow(LoadingStatus.LOADING)
    val status = _status.asStateFlow()

    private val _statusMessage = MutableStateFlow(Event(""))
    val statusMessage = _statusMessage.asStateFlow()

    private val _themeColor = MutableStateFlow(getRandomColor(initial = true))
    val themeColor = _themeColor.asStateFlow()

    private val _activeTags = MutableStateFlow(mutableListOf<String>())
    val activeTags = _activeTags.asStateFlow()

    private val _quotes = MutableStateFlow(mutableListOf<Quote>())
    val quotes = _quotes.asStateFlow()

    val currentQuoteModel: Quote?
        get() = _quotes.value.getOrNull(_currentQuoteViewIndex.value)

    init {
        getQuotes(initial = true)
    }

    /**
     * @param initial Whether quotes are fetched from init.
     * Get the quotes from the data repository and set them in the ViewModel
     **/
    fun getQuotes(initial: Boolean = false) {
        allQuotesFetched = false
        viewModelScope.launch(if (initial) Dispatchers.Main else Dispatchers.IO) {
            _status.value = LoadingStatus.LOADING
            try {
                val quotesResponse = quotesRepository.getQuotes()
                _quotes.value.clear()
                _quotes.value.addAll(quotesResponse)
                _currentQuoteViewIndex.value = 0
                _status.value = LoadingStatus.DONE
                _statusMessage.value = Event("Fetched quotes")
            } catch (e: Exception) {
                Log.e(TAG, e.stackTraceToString())
                _statusMessage.value = Event("Error getting quotes.")
                _status.value = LoadingStatus.ERROR
            }
        }
    }

    /**
     * Navigate to the next quote.
     * Also checks in if there is need for prefetching.
     */
    fun nextQuote() {
        if (_quotes.value.size - _currentQuoteViewIndex.value < 10
            && _status.value != LoadingStatus.PRELOAD
            && !allQuotesFetched
        ) {
            prefetchQuotes()
        }
        _themeColor.value = getRandomColor()
        if (_currentQuoteViewIndex.value < _quotes.value.size - 1) {
            _currentQuoteViewIndex.value += 1
        } else {
            _currentQuoteViewIndex.value = 0
        }
    }

    /**
     * Navigate to the previous quote.
     */
    fun prevQuote() {
        if (_currentQuoteViewIndex.value > 0) {
            _currentQuoteViewIndex.value -= 1
            _themeColor.value = getRandomColor()
        }
    }

    // Done: TODO: Update the repository to handle tagged quotes
    /**
     * Get quotes according to the selected tags.
     * Disallows selecting more than 3 tags.
     * @param tag Adds the tag to active tags or removes it if already present.
     */
    fun getTaggedQuotes(tag: String) {
        var tagRemoved = false
        allQuotesFetched = false
        if (_activeTags.value.contains(tag)) {
            _activeTags.value.remove(tag)
            tagRemoved = true
        } else {
            if (_activeTags.value.size > 2) {
                _statusMessage.value = Event("Cannot select more than 3 tags.")
                return
            }
            _activeTags.value.add(tag)
        }
        viewModelScope.launch(Dispatchers.IO) {
            _status.value = LoadingStatus.LOADING
            try {
                val apiResp =
                    quotesRepository.getTaggedQuotes(_activeTags.value)
                if (apiResp.quotesPresent == 0) {
                    _status.value = LoadingStatus.ERROR
                    _statusMessage.value = Event("No quotes present for selected tags.")
                } else {
                    _quotes.value.clear()
                    _quotes.value.addAll(apiResp.quotes)
                    _currentQuoteViewIndex.value = 0
                }
            } catch (e: Exception) {
                if (tagRemoved) _activeTags.value.add(tag) else _activeTags.value.remove(tag)
                _statusMessage.value =
                    Event("You need an active internet connection for getting tagged quotes.")
            }
            _status.value = LoadingStatus.DONE
        }
    }

    /**
     * Loads the next random quotes before reaching the end.
     * So he user can always navigate to next.
     */
    private fun prefetchQuotes() {
        Log.d(TAG, "Prefetching next quotes")
        viewModelScope.launch(Dispatchers.IO) {
            _status.value = LoadingStatus.PRELOAD
            try {
                val newQuotes = if (_activeTags.value.isNotEmpty()) {
                    quotesRepository.getTaggedQuotes(_activeTags.value).quotes
                } else {
                    quotesRepository.getQuotes()
                }
                newQuotes.filter { it.quoteId !in _quotes.value.map { it1 -> it1.quoteId } }
                if (newQuotes.isEmpty()) {
                    allQuotesFetched = true
                }
                _quotes.value.addAll(newQuotes)
                _status.value = LoadingStatus.DONE
            } catch (e: Exception) {
                Log.e(TAG, e.stackTraceToString())
            }
        }
    }

    /**
     * Returns a new color every time.
     * Checks recursively with the previously returned color.
     * */
    private fun getRandomColor(initial: Boolean = false): Color {
        val randomColor = pastelColors.values.random()
        if (!initial) {
            return if (randomColor == _themeColor.value) getRandomColor() else randomColor
        }
        return randomColor
    }

    companion object {
        private var allQuotesFetched = false
        private const val TAG = "HOME VIEWMODEL"
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
