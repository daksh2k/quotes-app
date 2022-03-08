package com.example.quotesapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.quotesapp.home.HomeViewModel
import com.example.quotesapp.ui.components.QuoteCard
import com.example.quotesapp.ui.theme.QuotesAppTheme

class MainActivity : ComponentActivity() {

    private val homeViewModel by viewModels<HomeViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
          QuotesApp(homeViewModel)
        }
    }
}

@Composable
fun QuotesApp(viewModel: HomeViewModel) {
    QuotesAppTheme{
        Surface() {
            Scaffold(modifier = Modifier
            ) {
                QuoteCard(
                    loadingStatus = viewModel.status,
                    currentQuote = viewModel.currentQuote,
                    totalAvailable = viewModel.quotes.size,
                    currentIndex = viewModel.quotes.indexOf(viewModel.currentQuote),
                    onBackClick = viewModel::prevQuote,
                    onForwardClick = viewModel::nextQuote
                )
            }
        }
    }
}