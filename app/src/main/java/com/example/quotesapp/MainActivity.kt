package com.example.quotesapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.quotesapp.ui.home.QuotesApp
import com.example.quotesapp.utils.provideHomeViewModelFactory

class MainActivity : ComponentActivity() {

    private val homeViewModel: HomeViewModel by viewModels {
        provideHomeViewModelFactory(applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QuotesApp(homeViewModel)
        }
    }
}

