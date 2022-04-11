package com.example.quotesapp

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.example.quotesapp.data.model.Quote
import com.example.quotesapp.ui.home.QuotesSingleViewLayout
import com.example.quotesapp.ui.theme.Purple200
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class HomeScreenSingleViewTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setup() {
        composeTestRule.setContent {
            QuotesSingleViewLayout(
                loadingStatus = LoadingStatus.DONE,
                allQuotes = listOf(
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
                ),
                currentQuote = Quote(
                    quoteId = "1234",
                    author = "Albert Einstein",
                    quote = "The only reason for time is so that everything doesn't happen at once",
                    source = "",
                    tags = "live, laugh, love, happy"
                ),
                themeColor = Purple200,
                activeTags = listOf("some", "thing"),
                onTagClick = { },
                onBack = { },
                onForward = { }
            )
        }
    }

    @Test
    fun singleView_TagBar() {
        composeTestRule
            .onNodeWithText("some")
            .assertIsDisplayed()
    }

    @Test
    fun singleView_NavigationButtonForward() {
        composeTestRule
            .onNode(
                hasAnyChild(
                    hasContentDescription("Go Forward")
                ), useUnmergedTree = true
            )
            .assertIsEnabled()
    }

    @Test
    fun singleView_NavigationButtonBack() {
        composeTestRule
            .onNode(
                hasAnyChild(
                    hasContentDescription("Go Back")
                ),
                useUnmergedTree = true
            )
            .assertIsNotEnabled()
    }

}