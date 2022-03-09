package com.example.quotesapp.ui.components

import android.content.Intent
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
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
import com.example.quotesapp.home.QuoteApiStatus
import com.example.quotesapp.network.Quote
import com.example.quotesapp.ui.theme.Purple200
import com.example.quotesapp.ui.theme.QuotesAppTheme
import com.example.quotesapp.utils.brightenColor
import com.example.quotesapp.utils.darkenColor
import com.example.quotesapp.utils.getValidTags


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
                    QuoteText(text = "Fetching Quotes!", themeColor = color)
                    if (loadingStatus == QuoteApiStatus.ERROR) {
                        Text(
                            text = "Error in fetching\nCheck your network connection!",
                            color = MaterialTheme.colors.error
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun QuoteText(
    text: String,
    themeColor: Color = MaterialTheme.colors.primary
) {
    Row(
        modifier = Modifier.padding(12.dp)
    ) {
        val scroll = rememberScrollState(0)
        Icon(
            imageVector = Icons.Default.FormatQuote,
            modifier = Modifier
                .rotate(180F)
                .size(45.dp),
            tint = themeColor,
            contentDescription = "Quote Icon"
        )
        Text(
            text = text,
            modifier = Modifier
                .padding(4.dp)
                .verticalScroll(scroll),
            color = themeColor,
            style = MaterialTheme.typography.h5.copy(fontWeight = FontWeight.W400)
        )
    }
}

@Composable
fun TagBarRow(
    modifier: Modifier = Modifier,
    themeColor: Color,
    tags: List<String>,
    activeTags: List<String>,
    onTagClick: (text: String) -> Unit
) {
    LazyRow(modifier = modifier.padding(horizontal = 8.dp)) {
        items(activeTags) { tag ->
            TagButton(
                modifier = Modifier
                    .padding(4.dp)
                    .clip(RoundedCornerShape(45)),
                butColors = ButtonDefaults.buttonColors(
                    backgroundColor = if (isSystemInDarkTheme()) darkenColor(
                        themeColor,
                        0.5f
                    ) else brightenColor(themeColor, 0.5f),
                    contentColor = if (!isSystemInDarkTheme()) darkenColor(themeColor) else brightenColor(
                        themeColor
                    ),
                ),
                text = tag,
                onTagClick = onTagClick
            )
        }

        items(tags) { tag ->
            TagButton(
                modifier = Modifier
                    .padding(4.dp)
                    .border(1.dp, themeColor, RoundedCornerShape(5)),
                text = tag,
                butColors = ButtonDefaults.buttonColors(
                    backgroundColor = themeColor.copy(0.1f),
                    contentColor = themeColor
                ),
                enabled = activeTags.size < 3,
                onTagClick = onTagClick
            )
        }
    }
}

@Composable
fun TagButton(
    modifier: Modifier = Modifier,
    text: String,
    butColors: ButtonColors,
    enabled: Boolean = true,
    onTagClick: (text: String) -> Unit
) {
    Button(
        onClick = { onTagClick(text) },
        elevation = null,
        enabled = enabled,
        colors = butColors,
        modifier = modifier
    ) {
        Text(
            text = text
        )
    }
}

@Composable
fun BottomToolBar(
    totalAvailable: Int,
    currentIndex: Int,
    onShare: () -> Unit,
    onBack: () -> Unit,
    onForward: () -> Unit,
    themeColor: Color,
    modifier: Modifier = Modifier
) {
    val buttonMods = Modifier
        .padding(8.dp)
        .clip(RoundedCornerShape(30))

    Row(modifier = modifier) {
        ShareQuoteButtonRow(
            onShareClick = onShare,
            buttonMods = buttonMods,
            themeColor = themeColor
        )
        NavigateQuoteButtonRow(
            modifier = Modifier.weight(1f),
            buttonMods = buttonMods,
            themeColor = themeColor,
            onBackClick = onBack,
            onForwardClick = onForward,
            showBack = currentIndex != 0,
            showForward = currentIndex - 1 != totalAvailable,
        )
    }
}

@Composable
fun ShareQuoteButtonRow(
    onShareClick: () -> Unit,
    buttonMods: Modifier = Modifier,
    themeColor: Color
) {
    Row(horizontalArrangement = Arrangement.Start) {
        IconButton(
            modifier = buttonMods.background(themeColor),
            onClick = { onShareClick() }
        ) {
            Icon(
                imageVector = Icons.Default.Share,
                contentDescription = "Share Quote"
            )
        }
    }
}

/**
 * A navigation toolbar with back and forward buttons
 */
@Composable
fun NavigateQuoteButtonRow(
    modifier: Modifier = Modifier,
    buttonMods: Modifier = Modifier,
    themeColor: Color,
    onBackClick: () -> Unit,
    onForwardClick: () -> Unit,
    showBack: Boolean = true,
    showForward: Boolean = true
) {
    Row(horizontalArrangement = Arrangement.End, modifier = modifier) {
        val iconMods = Modifier.requiredSize(35.dp)
        IconButton(
            onClick = onBackClick,
            enabled = showBack,
            modifier = buttonMods
                .alpha(if (showBack) 1f else 0.4f)
                .background(themeColor)
        ) {
            Icon(
                imageVector = Icons.Default.ChevronLeft,
                contentDescription = "Go Back",
                modifier = iconMods
            )
        }
        IconButton(
            onClick = onForwardClick,
            enabled = showForward,
            modifier = buttonMods
                .alpha(if (showForward) 1f else 0.4f)
                .background(themeColor)
        ) {
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Go Back",
                modifier = iconMods
            )
        }
    }
}

fun createShareIntent(text: String): Intent {
    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, text)
        type = "text/plain"
    }
    return Intent.createChooser(sendIntent, null)
}

@Preview(showBackground = true)
@Composable
fun PreviewTagBarRow() {
    TagBarRow(
        themeColor = Purple200,
        tags = listOf("life", "laugh"),
        activeTags = listOf("some", "thing"),
        onTagClick = {})
}

@Preview(showBackground = true, name = "Dark Mode", uiMode = UI_MODE_NIGHT_YES)
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

@Preview(showBackground = true, name = "Error", uiMode = UI_MODE_NIGHT_YES)
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