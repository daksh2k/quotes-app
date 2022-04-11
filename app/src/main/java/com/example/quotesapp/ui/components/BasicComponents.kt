package com.example.quotesapp.ui.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.quotesapp.ui.theme.QuotesAppTheme
import com.example.quotesapp.utils.PLACEHOLDER_HEIGHT
import kotlin.random.Random

/**
 * Main Quote text along with a Quote Icon.
 */
@Composable
fun QuoteText(
    text: String,
    themeColor: Color = MaterialTheme.colors.primary
) {
    Row(
        modifier = Modifier.padding(12.dp)
    ) {
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
                .padding(2.dp),
            color = themeColor,
            style = MaterialTheme.typography.h5.copy(fontWeight = FontWeight.W400)
        )
    }
}


@Composable
fun TagButton(
    modifier: Modifier = Modifier,
    text: String,
    shape: Shape = RoundedCornerShape(45.dp),
    border: BorderStroke = BorderStroke(0.dp, Color.Transparent),
    butColors: ButtonColors,
    enabled: Boolean = true,
    onTagClick: (text: String) -> Unit
) {
    Button(
        onClick = { onTagClick(text) },
        border = border,
        elevation = null,
        shape = shape,
        enabled = enabled,
        colors = butColors,
        modifier = modifier.padding(4.dp)
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
                contentDescription = "Go Forward",
                modifier = iconMods
            )
        }
    }
}


/**
 * Shows the loading state of the quote card.
 */
@Composable
fun LoadingCard() {
    // Creates an `InfiniteTransition` that runs infinite child animation values.
    val infiniteTransition = rememberInfiniteTransition()
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        // `infiniteRepeatable` repeats the specified duration-based `AnimationSpec` infinitely.
        animationSpec = infiniteRepeatable(
            // The `keyframes` animates the value by specifying multiple timestamps.
            animation = keyframes {
                // One iteration is 1000 milliseconds.
                durationMillis = 1000
                // 0.7f at the middle of an iteration.
                0.7f at 500
            },
            // When the value finishes animating from 0f to 1f, it repeats by reversing the
            // animation direction.
            repeatMode = RepeatMode.Reverse
        )
    )
    val minHeight = remember { Random.nextInt(160, 220).dp }
    val loadingColor = if (isSystemInDarkTheme()) Color.DarkGray else Color.LightGray
    Box(
        Modifier
            .clip(shape = RoundedCornerShape(15.dp))
            .border(
                width = 5.dp,
                color = MaterialTheme.colors.onPrimary,
                shape = RoundedCornerShape(15.dp)
            )
            .requiredWidthIn(max = LocalConfiguration.current.screenWidthDp.dp.minus(20.dp))
    ) {
        Column(
            modifier = Modifier
                .padding(5.dp)
                .background(color = MaterialTheme.colors.onPrimary)
                .heightIn(min = minHeight)
                .fillMaxWidth()

        ) {
            Row(
                modifier = Modifier.padding(2.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.FormatQuote,
                    modifier = Modifier
                        .rotate(180F)
                        .size(35.dp),
                    contentDescription = "Quote Icon"
                )
                Column {
                    Spacer(modifier = Modifier.height(18.dp))
                    LoadingPlaceholder(alpha, loadingColor)
                    Spacer(modifier = Modifier.height(PLACEHOLDER_HEIGHT))
                    LoadingPlaceholder(alpha, loadingColor)
                    Spacer(modifier = Modifier.height(PLACEHOLDER_HEIGHT))
                    LoadingPlaceholder(alpha, loadingColor)
                    Spacer(modifier = Modifier.height(PLACEHOLDER_HEIGHT))
                    LoadingPlaceholder(alpha, loadingColor)
                    Spacer(modifier = Modifier.height(PLACEHOLDER_HEIGHT))
                    Box(
                        modifier = Modifier
                            .align(Alignment.End)
                            .height(10.dp)
                            .width(70.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(loadingColor.copy(alpha = alpha))
                    )
                }

            }

        }
    }
}

@Composable
fun LoadingPlaceholder(alpha: Float, loadingColor: Color) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(10.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(loadingColor.copy(alpha = alpha))
    )
}

/**
 * Simple Loading Progress Indicator.
 * With Icon and text.
 */
@Composable
fun LoadingProgressIndicator() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(25.dp))
        CircularProgressIndicator(color = MaterialTheme.colors.onPrimary)
        Spacer(Modifier.height(10.dp))
        Text("Loading More...", color = MaterialTheme.colors.onPrimary)
    }
}

@Preview(showBackground = true)
@Preview(name = "Dark Theme", uiMode = UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun PreviewLoadingCard() {
    QuotesAppTheme {
        LoadingCard()
    }
}
