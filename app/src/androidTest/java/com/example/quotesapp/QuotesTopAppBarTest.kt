package com.example.quotesapp

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import com.example.quotesapp.ui.components.QuotesTopAppBar
import com.example.quotesapp.ui.theme.Purple200
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class QuotesTopAppBarTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setup() {
        composeTestRule.setContent {
            QuotesTopAppBar(
                forceReload = { },
                loadingStatus = LoadingStatus.DONE,
                themeColor = Purple200,
                listLayout = true,
                onListClick = { }
            )
        }
    }

    @Test
    fun topBar_ReloadButtonDisplayed() {
        composeTestRule
            .onNodeWithContentDescription("Reload quotes")
            .assertIsDisplayed()
    }

    @Test
    fun topBar_SwitchLayoutDisplayed() {
        composeTestRule
            .onNodeWithContentDescription("Switch to Single View")
            .assertIsDisplayed()
    }
}