package com.example.quotesapp

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class HomeViewModelTest {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var homeRepository: FakeRepository

//    @get:Rule
//    var mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setupViewModel() {
        homeRepository = FakeRepository()
        homeViewModel = HomeViewModel(homeRepository)
    }

    @Test
    fun quotesAreLoaded() = runBlockingTest {
        homeViewModel.quotes.test {
            val result = awaitItem()
            assertThat(result.size).isEqualTo(2)
            assertThat(result[0].tags).contains("live")
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun getStatusMessageTest() = runBlockingTest {
        homeViewModel.statusMessage.test {
            assertThat(awaitItem().peekContent()).contains("Fetched")
        }
    }

    @Test
    fun taggedQuotes_AreUpdated() = runBlockingTest {
        homeViewModel.activeTags.test {
            homeViewModel.getTaggedQuotes("some")
            assertThat(awaitItem()).containsExactly("some")
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun loadingStatus_isUpdated() = runBlockingTest {
        val testResults = mutableListOf<LoadingStatus>()
        val job = launch {
            homeViewModel.status.toList(testResults)
        }
        homeViewModel.getTaggedQuotes("some")
        assertThat(testResults).isEqualTo(
            listOf(
                LoadingStatus.DONE,
                LoadingStatus.LOADING,
                LoadingStatus.DONE
            )
        )
        job.cancel()
    }

    @Test
    fun activeTags_areUpdated() = runBlockingTest {
//        val testResults = mutableListOf<List<String>>()
//        val job = launch {
//            homeViewModel.activeTags.toList(testResults)
//        }
        assertThat(homeViewModel.activeTags.value).isEmpty()
        homeViewModel.getTaggedQuotes("some")
        assertThat(homeViewModel.activeTags.value).containsExactly("some")
        homeViewModel.getTaggedQuotes("other")
        assertThat(homeViewModel.activeTags.value).containsExactly("some", "other")
        homeViewModel.getTaggedQuotes("some")
        assertThat(homeViewModel.activeTags.value).containsExactly("other")
//        job.cancel()
    }

    @Test
    fun `test quote change on next quote`() = runBlockingTest {
        assertThat(homeViewModel.currentQuoteModel?.quoteId).isEqualTo("1234")
        homeViewModel.nextQuote()
        assertThat(homeViewModel.currentQuoteModel?.quoteId).isEqualTo("5678")
    }

    @Test
    fun `test quote change on previous quote`() = runBlockingTest {
        assertThat(homeViewModel.currentQuoteModel?.quoteId).isEqualTo("1234")
        homeViewModel.nextQuote()
        assertThat(homeViewModel.currentQuoteModel?.quoteId).isEqualTo("5678")
        homeViewModel.prevQuote()
        assertThat(homeViewModel.currentQuoteModel?.quoteId).isEqualTo("1234")
    }
}