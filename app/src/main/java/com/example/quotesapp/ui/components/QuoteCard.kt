package com.example.quotesapp.ui.components

import android.content.res.Configuration
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
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
import com.example.quotesapp.LoadingStatus
import com.example.quotesapp.R
import com.example.quotesapp.data.model.Quote
import com.example.quotesapp.data.model.getTagsList
import com.example.quotesapp.ui.theme.Purple200
import com.example.quotesapp.ui.theme.QuotesAppTheme
import com.example.quotesapp.utils.getValidTags

/**
 * Main quote card composable with slots for changing content in the bottom bar.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun QuoteCard(
    loadingStatus: LoadingStatus = LoadingStatus.DONE,
    showActiveTagsHeading: Boolean = true,
    currentQuoteModel: Quote? = null,
    themeColor: Color = Purple200,
    activeTags: List<String> = listOf(),
    onTagClick: (text: String) -> Unit = {},
    bottomRowContent: @Composable () -> Unit = {}
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
        var formattedQuote = ""
        val context = LocalContext.current
        if (loadingStatus == LoadingStatus.DONE && currentQuoteModel != null) {
            formattedQuote = stringResource(
                R.string.quote,
                currentQuoteModel.quote,
                currentQuoteModel.author,
                "#" + currentQuoteModel.tags.replace(", ", " #")
            )
        }

        Column(
            modifier = Modifier
                .padding(3.dp)
                .background(color = MaterialTheme.colors.onPrimary)
                .fillMaxWidth()
                .combinedClickable(
                    onClick = {},
                    onLongClick = { context.startActivity(createShareIntent(formattedQuote)) }
                )

        ) {
            when (loadingStatus) {
                LoadingStatus.ERROR -> QuoteText(
                    text = stringResource(R.string.error_text),
                    themeColor = MaterialTheme.colors.error
                )
                LoadingStatus.LOADING -> QuoteText(
                    text = stringResource(R.string.loading_text),
                    themeColor = themeColor
                )
                else -> {
                    if (activeTags.isNotEmpty() && showActiveTagsHeading) {
                        Text(
                            text = activeTags.joinToString(
                                prefix = "#",
                                postfix = " quotes",
                                separator = ", #"
                            ),
                            modifier = Modifier
                                .padding(6.dp)
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
                        tags = getValidTags(currentQuoteModel.getTagsList(), activeTags),
                        activeTags = activeTags,
                        onTagClick = onTagClick
                    )
                    bottomRowContent()
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
            currentQuoteModel = Quote(
                quoteId = "",
                author = "Albert Einstein",
                quote = "The only reason for time is so that everything doesn't happen at once",
                source = "",
                tags = "live, laugh, love, happy"
            ),
            activeTags = listOf("happy")
        )
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
            activeTags = listOf("happy")
        )
    }
}