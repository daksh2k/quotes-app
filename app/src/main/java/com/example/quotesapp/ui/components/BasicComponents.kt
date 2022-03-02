package com.example.quotesapp.ui.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quotesapp.ui.theme.QuotesAppTheme


@Composable
fun QuoteCard(){
    Card {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            QuoteText(text = "slkdnfksa sld fsld fsdfldsssssssss fdddddddddddddf")
            Text(
                text = "sdljnfslj sd fjksd f ksdf s",
                modifier = Modifier.align(alignment = Alignment.End),
                style = MaterialTheme.typography.body1.copy(
                    fontWeight = FontWeight.W200,
                    fontSize = 14.sp
                )
            )
            BottomToolBar(
                onShare = { /*TODO*/ },
                onBack = { /*TODO*/ },
                onForward = { /*TODO*/ },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun QuoteText(
    text: String
){
    Row(){
        Icon(
            imageVector = Icons.Default.FormatQuote,
            modifier = Modifier.rotate(180F),
            contentDescription = "Quote Icon"
        )
        Text(
            text = text,
            modifier = Modifier.padding(6.dp),
            style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.W400)
        )
    }
}

@Composable
fun BottomToolBar(
    onShare: () -> Unit,
    onBack: () -> Unit,
    onForward: () -> Unit,
    modifier: Modifier = Modifier
){
    Row(modifier = modifier) {
        ShareQuoteButtonRow(
            onShareClick = onShare
        )
        NavigateQuoteButtonRow(
            onBackClick = onBack,
            onForwardClick = onForward,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun ShareQuoteButtonRow(
    onShareClick: () -> Unit,
    modifier: Modifier = Modifier
){
    Row(modifier = modifier, horizontalArrangement = Arrangement.Start) {
        IconButton(onClick = onShareClick) {
            Icon(imageVector = Icons.Default.Share, contentDescription = "Share Quote")
        }
    }
}

@Composable
fun NavigateQuoteButtonRow(
    onBackClick: () -> Unit,
    onForwardClick: () -> Unit,
    showBack: Boolean = true,
    showForward: Boolean = true,
    modifier: Modifier = Modifier
){
    Row(modifier = modifier, horizontalArrangement = Arrangement.End){
        IconButton(
            onClick = onBackClick,
            enabled = showBack,
            modifier = Modifier.padding(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ChevronLeft,
                contentDescription = "Go Back"
            )
        }
        IconButton(
            onClick = onForwardClick,
            enabled = showForward,
            modifier = Modifier.padding(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Go Back"
            )
        }
    }
}



@Preview(showBackground = true, name = "Dark Mode",uiMode = UI_MODE_NIGHT_YES)
@Preview(showBackground = true, name = "Light Mode")
@Composable
fun QuoteCardPreview(){
    QuotesAppTheme {
        QuoteCard()
    }
}