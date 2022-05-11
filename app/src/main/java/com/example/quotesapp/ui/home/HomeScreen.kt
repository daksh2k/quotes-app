package com.example.quotesapp.ui.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.quotesapp.HomeViewModel
import com.example.quotesapp.LoadingStatus
import com.example.quotesapp.R
import com.example.quotesapp.data.SettingsDataStore
import com.example.quotesapp.data.dataStore
import com.example.quotesapp.data.model.Quote
import com.example.quotesapp.ui.components.*
import com.example.quotesapp.ui.theme.Purple200
import com.example.quotesapp.ui.theme.QuotesAppTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.launch


/**
 * Main entry point for app.
 * Manages the top bar and both single and list view layouts.
 * @param viewModel The main viewModel which contains all the implementations and data.
 */
@Composable
fun QuotesApp(viewModel: HomeViewModel) {
    QuotesAppTheme {

        // Remember scaffold state to show snackbar on status change
        val scaffoldState: ScaffoldState = rememberScaffoldState()
        val statusMessage by viewModel.statusMessage.collectAsState()

        // Show snackbar when there is a change in message and message is not empty.
        if (!statusMessage.hasBeenHandled && statusMessage.peekContent().isNotEmpty()) {
            LaunchedEffect(key1 = statusMessage) {
                scaffoldState.snackbarHostState.showSnackbar(statusMessage.getContentIfNotHandled()!!)
            }
        }

        val appContext = LocalContext.current
        val scope = rememberCoroutineScope()

        //Get the preferences data store instance which stores the layout preference
        val dataStore = SettingsDataStore(appContext.dataStore)
        val layoutState = dataStore.preferenceFlow.collectAsState(false)

        // Get the relevant variables from viewmodel
        val loadingStatus by viewModel.status.collectAsState()
        val themeColor by viewModel.themeColor.collectAsState()
        val activeTags by viewModel.activeTags.collectAsState()
        val allQuotes by viewModel.quotes.collectAsState()


        val color: Color by animateColorAsState(
            targetValue = themeColor,
            animationSpec = tween(
                durationMillis = 1200,
                easing = LinearOutSlowInEasing
            )
        )

        // Remember a SystemUiController to match status bar with theme color
        val systemUiController = rememberSystemUiController()
        systemUiController.setStatusBarColor(
            color = color
        )

        // Remember a lazy list state to respond to events on scroll
        val lazyListState = rememberLazyListState()

        Scaffold(
            scaffoldState = scaffoldState,
            topBar = {
                AnimatedVisibility(
                    visible = !layoutState.value || (lazyListState.firstVisibleItemIndex == 0 || lazyListState.isScrollingUp()),
//                    enter = slideInVertically(
//                        // Enters by sliding in from offset -fullHeight to 0.
//                        initialOffsetY = { fullHeight -> -fullHeight / 4 },
//                        animationSpec = tween(durationMillis = 200, easing = LinearEasing)
//                    ),
//                    exit = slideOutVertically(
//                        // Exits by sliding out from offset 0 to -fullHeight.
//                        targetOffsetY = { fullHeight -> -fullHeight / 4 },
//                        animationSpec = tween(durationMillis = 200, easing = LinearEasing)
//                    )
                ) {
//                if(!layoutState.value || lazyListState.isScrollingUp()){
                    QuotesTopAppBar(
                        forceReload = viewModel::getQuotes,
                        loadingStatus = loadingStatus,
                        themeColor = color,
                        listLayout = layoutState.value,
                        onListClick = {
                            scope.launch {
                                dataStore.saveLayoutPref(
                                    !layoutState.value,
                                    appContext
                                )
                            }
                        }
                    )
                }
            },
            modifier = Modifier
        ) {
            if (layoutState.value) {
                QuotesListViewLayout(
                    loadingStatus = loadingStatus,
                    listState = lazyListState,
                    allQuotes = allQuotes,
                    themeColor = color,
                    activeTags = activeTags,
                    onTagClick = viewModel::getTaggedQuotes,
                    onScrollListener = viewModel::checkScroll
                )
            } else {
                QuotesSingleViewLayout(
                    loadingStatus = loadingStatus,
                    allQuotes = allQuotes,
                    currentQuote = viewModel.currentQuoteModel,
                    themeColor = color,
                    activeTags = activeTags,
                    onTagClick = viewModel::getTaggedQuotes,
                    onBack = viewModel::prevQuote,
                    onForward = viewModel::nextQuote
                )
            }
        }
    }
}

/**
 * Default single view layout with navigation buttons and Tag Bar.
 */
@Composable
fun QuotesSingleViewLayout(
    loadingStatus: LoadingStatus,
    allQuotes: List<Quote>,
    currentQuote: Quote?,
    themeColor: Color,
    activeTags: List<String>,
    onTagClick: (text: String) -> Unit,
    onBack: () -> Unit,
    onForward: () -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(color = themeColor)
    ) {
        QuoteCard(
            loadingStatus = loadingStatus,
            currentQuoteModel = currentQuote,
            themeColor = themeColor,
            activeTags = activeTags,
            onTagClick = onTagClick
        ) {
            val context = LocalContext.current
            val formattedQuote = stringResource(
                R.string.quote,
                currentQuote!!.quote,
                currentQuote.author,
                "#" + currentQuote.tags.replace(", ", " #")
            )
            BottomToolBar(
                totalAvailable = allQuotes.size,
                currentIndex = allQuotes.indexOf(currentQuote),
                onShare = { context.startActivity(createShareIntent(formattedQuote)) },
                onBack = onBack,
                onForward = onForward,
                themeColor = themeColor,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }

}

/**
 * Default List View Layout to show multiple quotes together.
 */
@Composable
fun QuotesListViewLayout(
    loadingStatus: LoadingStatus,
    allQuotes: List<Quote>,
    listState: LazyListState,
    themeColor: Color,
    activeTags: List<String>,
    onTagClick: (text: String) -> Unit,
    onScrollListener: (itemIndex: Int) -> Unit
) {
    if (listState.isScrollInProgress) {
        onScrollListener(listState.firstVisibleItemIndex)
    }
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(color = themeColor)
    ) {
        Column {
//            if(activeTags.isNotEmpty() && listState.isScrollingUp()){
            AnimatedVisibility(activeTags.isNotEmpty()) {
                TagBarRow(
                    modifier = Modifier
                        .padding(5.dp)
                        .fillMaxWidth()
                        .clip(shape = RoundedCornerShape(15.dp))
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colors.onPrimary,
                            shape = RoundedCornerShape(15.dp)
                        )
                        .background(MaterialTheme.colors.onPrimary),
                    themeColor = themeColor,
                    tags = listOf(),
                    activeTags = activeTags,
                    onTagClick = onTagClick
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            LazyColumn(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                state = listState,
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.padding(8.dp)
            ) {
                when (loadingStatus) {
                    LoadingStatus.LOADING -> items(List(20) { "$it" }) { LoadingCard() }
                    LoadingStatus.ERROR -> item { QuoteCard(loadingStatus) }
                    else -> {
                        items(allQuotes) { quote ->
                            QuoteCard(
                                showActiveTagsHeading = false,
                                currentQuoteModel = quote,
                                themeColor = themeColor,
                                activeTags = activeTags,
                                onTagClick = onTagClick
                            )
                        }
                    }
                }
                if (loadingStatus == LoadingStatus.PRELOAD) {
                    item {
                        LoadingProgressIndicator()
                    }
                }

            }
        }
    }
}

@Composable
@Preview
fun QuotesSingleViewLayoutPreview() {
    QuotesAppTheme {
        QuotesSingleViewLayout(
            loadingStatus = LoadingStatus.DONE,
            allQuotes = listOf(),
            currentQuote = Quote(
                quoteId = "",
                author = "Albert Einstein",
                quote = "The only reason for time is so that everything doesn't happen at once",
                source = "",
                tags = "live, laugh, love, happy"
            ),
            themeColor = Purple200,
            activeTags = listOf("life"),
            onTagClick = {},
            onBack = {},
            onForward = { }
        )
    }
}

@Composable
@Preview
fun QuotesListViewLayoutPreview() {
    QuotesAppTheme {
        QuotesListViewLayout(
            loadingStatus = LoadingStatus.DONE,
            listState = rememberLazyListState(),
            allQuotes = List(30) {
                Quote(
                    quoteId = "",
                    author = "Albert Einstein",
                    quote = "The only reason for time is so that everything doesn't happen at once",
                    source = "",
                    tags = "live, laugh, love, happy"
                )
            },
            themeColor = Purple200,
            activeTags = listOf("some"),
            onTagClick = {},
            onScrollListener = {}
        )
    }
}
