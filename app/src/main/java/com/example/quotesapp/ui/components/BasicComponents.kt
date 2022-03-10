package com.example.quotesapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

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