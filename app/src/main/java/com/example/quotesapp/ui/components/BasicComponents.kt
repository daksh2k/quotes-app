package com.example.quotesapp.ui.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quotesapp.home.QuoteApiStatus
import com.example.quotesapp.network.Quote
import com.example.quotesapp.ui.theme.Purple200
import com.example.quotesapp.ui.theme.QuotesAppTheme


@Composable
fun QuoteCard(
    loadingStatus: QuoteApiStatus,
    currentQuote: Quote?,
    themeColor: Color = MaterialTheme.colors.primary,
    totalAvailable: Int,
    currentIndex: Int,
    onBackClick: () -> Unit,
    onForwardClick: () -> Unit
){
    Card {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .background(color = themeColor)
        ){
            Column(
                modifier = Modifier
                    .padding(3.dp)
                    .background(color = MaterialTheme.colors.onPrimary)
                    .fillMaxWidth(),

            ) {
                if(loadingStatus!=QuoteApiStatus.LOADING && loadingStatus!=QuoteApiStatus.ERROR){
                    QuoteText(
                        text = currentQuote?.quote ?: "Error"
                    )
                    Text(
                        text = currentQuote?.author ?: "Error",
                        modifier = Modifier
                            .align(alignment = Alignment.End)
                            .padding(12.dp),
                        style = MaterialTheme.typography.body1.copy(
                            fontWeight = FontWeight.W200,
                            fontSize = 14.sp
                        )
                    )
                    BottomToolBar(
                        totalAvailable = totalAvailable,
                        currentIndex = currentIndex,
                        onShare = { /*TODO*/ },
                        onBack = onBackClick,
                        onForward = onForwardClick,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                else{
                    QuoteText(text = "Fetching Quotes!")
                    if(loadingStatus==QuoteApiStatus.ERROR){
                        Text(text = "Error in fetching\nCheck your network connection!", color = MaterialTheme.colors.error)
                    }
                }
            }
        }
        }

}

@Composable
fun QuoteText(
    text: String
){
    Row(
        modifier = Modifier.padding(12.dp)
    ){
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
    totalAvailable: Int,
    currentIndex: Int,
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
            modifier = Modifier.weight(1f),
            onBackClick = onBack,
            onForwardClick = onForward,
            showBack = currentIndex!=0,
            showForward = currentIndex-1!=totalAvailable
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
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    onForwardClick: () -> Unit,
    showBack: Boolean = true,
    showForward: Boolean = true
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
        QuoteCard(
            loadingStatus = QuoteApiStatus.DONE,
            currentQuote = Quote(author = "Albert Einstein", quote = "The only reason for time is so that everything doesn't happen at once","",""),
            Purple200,5,1,{},{})
    }
}

@Preview(showBackground = true, name = "Error",uiMode = UI_MODE_NIGHT_YES)
@Composable
fun QuoteCardPreviewError(){
    QuotesAppTheme {
        QuoteCard(
            loadingStatus = QuoteApiStatus.ERROR,
            currentQuote = Quote(author = "Albert Einstein", quote = "The only reason for time is so that everything doesn't happen at once","",""),
            Purple200,5,1,{},{})
    }
}