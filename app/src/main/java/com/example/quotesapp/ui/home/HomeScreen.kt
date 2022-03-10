package com.example.quotesapp.home

import android.content.res.Configuration
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
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
import com.example.quotesapp.R
import com.example.quotesapp.network.Quote
import com.example.quotesapp.ui.components.BottomToolBar
import com.example.quotesapp.ui.components.QuoteText
import com.example.quotesapp.ui.components.TagBarRow
import com.example.quotesapp.ui.components.createShareIntent
import com.example.quotesapp.ui.theme.Purple200
import com.example.quotesapp.ui.theme.QuotesAppTheme
import com.example.quotesapp.utils.getValidTags

@Composable
fun QuotesApp(viewModel: HomeViewModel) {
    QuotesAppTheme {
        Scaffold(
            modifier = Modifier
        ) {
            QuoteCard(
                loadingStatus = viewModel.status,
                currentQuote = viewModel.currentQuote,
                themeColor = viewModel.themeColor,
                totalAvailable = viewModel.quotes.size,
                currentIndex = viewModel.quotes.indexOf(viewModel.currentQuote),
                activeTags = viewModel.activeTags,
                onBackClick = viewModel::prevQuote,
                onForwardClick = viewModel::nextQuote,
                onTagClick = viewModel::getTaggedQuotes
            )
        }
    }
}

@Composable
fun QuoteCard(
    loadingStatus: QuoteApiStatus,
    currentQuote: Quote?,
    themeColor: Color,
    totalAvailable: Int,
    currentIndex: Int,
    activeTags: List<String>,
    onBackClick: () -> Unit,
    onForwardClick: () -> Unit,
    onTagClick: (text: String) -> Unit
) {
    val color: Color by animateColorAsState(
        targetValue = themeColor,
        animationSpec = tween(
            durationMillis = 1200,
            easing = LinearOutSlowInEasing
        )
    )
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(color = color)
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
                if (loadingStatus != QuoteApiStatus.LOADING && loadingStatus != QuoteApiStatus.ERROR) {
                    val context = LocalContext.current
                    val formattedQuote = stringResource(
                        R.string.quote,
                        currentQuote!!.quote,
                        currentQuote.author,
                        "#" + currentQuote.tags.replace(", ", " #")
                    )
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
                            color = color,
                            textAlign = TextAlign.Center,
                            maxLines = 2
                        )
                    }
                    QuoteText(
                        text = currentQuote.quote,
                        themeColor = color
                    )
                    Text(
                        text = "- " + currentQuote.author,
                        color = color,
                        modifier = Modifier
                            .align(alignment = Alignment.End)
                            .padding(12.dp),
                        style = MaterialTheme.typography.body1.copy(
                            fontWeight = FontWeight.W300,
                            fontSize = 22.sp
                        )
                    )
                    TagBarRow(
                        themeColor = color,
                        tags = getValidTags(currentQuote.tags.split(", "), activeTags),
                        activeTags = activeTags,
                        onTagClick = onTagClick
                    )
                    BottomToolBar(
                        totalAvailable = totalAvailable,
                        currentIndex = currentIndex,
                        onShare = { context.startActivity(createShareIntent(formattedQuote)) },
                        onBack = onBackClick,
                        onForward = onForwardClick,
                        themeColor = color,
                        modifier = Modifier.fillMaxWidth()
                    )
                } else {
                    QuoteText(text = stringResource(R.string.loading_text), themeColor = color)
                    if (loadingStatus == QuoteApiStatus.ERROR) {
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
            loadingStatus = QuoteApiStatus.DONE,
            currentQuote = Quote(
                author = "Albert Einstein",
                quote = "The only reason for time is so that everything doesn't happen at once",
                "",
                ""
            ),
            Purple200, 5, 1, listOf(), {}, {}, {})
    }
}

@Preview(showBackground = true, name = "Error", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun QuoteCardPreviewError() {
    QuotesAppTheme {
        QuoteCard(
            loadingStatus = QuoteApiStatus.ERROR,
            currentQuote = Quote(
                author = "Albert Einstein",
                quote = "The only reason for time is so that everything doesn't happen at once",
                "",
                ""
            ),
            Purple200, 5, 1, listOf(), {}, {}, {})
    }
}