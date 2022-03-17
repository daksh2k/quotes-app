package com.example.quotesapp.ui.home

import android.content.res.Configuration
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quotesapp.HomeViewModel
import com.example.quotesapp.LoadingStatus
import com.example.quotesapp.R
import com.example.quotesapp.data.model.Quote
import com.example.quotesapp.ui.components.*
import com.example.quotesapp.ui.theme.Purple200
import com.example.quotesapp.ui.theme.QuotesAppTheme
import com.example.quotesapp.utils.getValidTags

@Composable
fun QuotesApp(viewModel: HomeViewModel) {
    val scaffoldState: ScaffoldState = rememberScaffoldState()

    QuotesAppTheme {

//        val scope = rememberCoroutineScope()

//        val abcc = LocalContext.current

        val statusMessage by viewModel.statusMessage.collectAsState()

        if (!statusMessage.hasBeenHandled && statusMessage.peekContent().isNotEmpty()) {
//            Toast.makeText(abcc,statusMessage.peekContent(),Toast.LENGTH_SHORT)
            LaunchedEffect(key1 = statusMessage) {
                scaffoldState.snackbarHostState.showSnackbar(statusMessage.getContentIfNotHandled()!!)
            }
        }
        Scaffold(
            scaffoldState = scaffoldState,
            topBar = {
                QuotesTopAppBar(
                    forceReload = viewModel::getQuotes,
                    loadingStatus = viewModel.status
                )
            },
            modifier = Modifier
        ) {
            val color: Color by animateColorAsState(
                targetValue = viewModel.themeColor,
                animationSpec = tween(
                    durationMillis = 1200,
                    easing = LinearOutSlowInEasing
                )
            )
            QuoteCard(
                loadingStatus = viewModel.status,
                currentQuoteModel = viewModel.currentQuoteModel,
                themeColor = color,
                activeTags = viewModel.activeTags,
                onTagClick = viewModel::getTaggedQuotes
            ) {
                val context = LocalContext.current
                val formattedQuote = stringResource(
                    R.string.quote,
                    viewModel.currentQuoteModel!!.quote,
                    viewModel.currentQuoteModel!!.author,
                    "#" + viewModel.currentQuoteModel!!.tags.replace(", ", " #")
                )
                val currentQuotes by viewModel.quotes.collectAsState()
                BottomToolBar(
                    totalAvailable = currentQuotes.size,
                    currentIndex = currentQuotes.indexOf(viewModel.currentQuoteModel),
                    onShare = { context.startActivity(createShareIntent(formattedQuote)) },
                    onBack = viewModel::prevQuote,
                    onForward = viewModel::nextQuote,
                    themeColor = color,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun QuoteCard(
    loadingStatus: LoadingStatus,
    currentQuoteModel: Quote?,
    themeColor: Color,
    activeTags: List<String>,
    onTagClick: (text: String) -> Unit,
    bottomRowContent: @Composable () -> Unit = {}
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(color = themeColor)
    ) {
        Box(
            Modifier
                .clip(shape = RoundedCornerShape(15.dp))
                .border(
                    width = 5.dp,
                    color = MaterialTheme.colors.onPrimary,
                    shape = RoundedCornerShape(15.dp)
                )
                .requiredWidthIn(max = LocalConfiguration.current.screenWidthDp.dp.minus(20.dp))
                .animateContentSize()
        ) {
            Column(
                modifier = Modifier
                    .padding(3.dp)
                    .background(color = MaterialTheme.colors.onPrimary)
                    .fillMaxWidth()

            ) {
//                if (loadingStatus == LoadingStatus.DONE || loadingStatus == LoadingStatus.ERROR) {
//                    IconButton(
//                        onClick = forceReload,
//                        modifier = Modifier.align(Alignment.End)
//                    ) {
//                        Icon(Icons.Default.Refresh, contentDescription = "Reload quotes")
//                    }
//                }
                if (loadingStatus != LoadingStatus.LOADING && loadingStatus != LoadingStatus.ERROR) {
                    if (activeTags.isNotEmpty()) {
                        Text(
                            text = activeTags.joinToString(
                                prefix = "#",
                                postfix = " quotes",
                                separator = ", #"
                            ),
                            modifier = Modifier
                                .padding(8.dp)
                                .align(alignment = Alignment.CenterHorizontally),
                            style = MaterialTheme.typography.h5,
                            color = themeColor,
                            textAlign = TextAlign.Center,
                            maxLines = 2
                        )
                    }
                    QuoteText(
                        text = currentQuoteModel!!.quote,
                        themeColor = themeColor
                    )
                    Text(
                        text = "- " + currentQuoteModel.author,
                        color = themeColor,
                        modifier = Modifier
                            .align(alignment = Alignment.End)
                            .padding(12.dp),
                        style = MaterialTheme.typography.body1.copy(
                            fontWeight = FontWeight.W300,
                            fontSize = 22.sp
                        )
                    )
                    TagBarRow(
                        themeColor = themeColor,
                        tags = getValidTags(currentQuoteModel.tags.split(", "), activeTags),
                        activeTags = activeTags,
                        onTagClick = onTagClick
                    )
                    bottomRowContent()

                } else {
                    QuoteText(text = stringResource(R.string.loading_text), themeColor = themeColor)
                    if (loadingStatus == LoadingStatus.ERROR) {
                        Text(
                            text = stringResource(R.string.error_text),
                            color = MaterialTheme.colors.error
                        )
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true, name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(showBackground = true, name = "Light Mode")
@Composable
fun QuoteCardPreview() {
    QuotesAppTheme {
        QuoteCard(
            loadingStatus = LoadingStatus.DONE,
            currentQuoteModel = Quote(
                quoteId = "",
                author = "Albert Einstein",
                quote = "The only reason for time is so that everything doesn't happen at once",
                source = "",
                tags = "live, laugh, love, happy"
            ),
            Purple200, listOf("happy"), {})
    }
}

@Preview(showBackground = true, name = "Error", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun QuoteCardErrorPreview() {
    QuotesAppTheme {
        QuoteCard(
            loadingStatus = LoadingStatus.ERROR,
            currentQuoteModel = Quote(
                quoteId = "",
                author = "Albert Einstein",
                quote = "The only reason for time is so that everything doesn't happen at once",
                source = "",
                tags = "live, laugh, love, happy"
            ),
            Purple200, listOf("happy"), {})
    }
}