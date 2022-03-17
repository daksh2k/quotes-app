package com.example.quotesapp.ui.components


import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.quotesapp.LoadingStatus
import com.example.quotesapp.ui.theme.QuotesAppTheme

@Composable
fun QuotesTopAppBar(
    forceReload: () -> Unit,
    loadingStatus: LoadingStatus
) {
    TopAppBar(
        title = { TopTitle() },
        modifier = Modifier.alpha(1f),
        actions = {
            if (loadingStatus == LoadingStatus.DONE || loadingStatus == LoadingStatus.ERROR) {
                IconButton(
                    onClick = forceReload,
//                    modifier = Modifier.align(Alignment.End)
                ) {
                    Icon(Icons.Default.Refresh, contentDescription = "Reload quotes")
                }
            }
        },
        backgroundColor = MaterialTheme.colors.onPrimary,
        contentColor = MaterialTheme.colors.onBackground,
        elevation = 10.dp
    )

}

@Composable
fun TopTitle() {
    Text("Quotes")
}

@Composable
@Preview(showBackground = true)
fun PreviewTopBar() {
    QuotesAppTheme {
        QuotesTopAppBar(
            forceReload = {},
            loadingStatus = LoadingStatus.DONE
        )
    }
}

//
//@Composable
//fun TopActions(
//    forceReload: () -> Unit,
//    loadingStatus: LoadingStatus
//): @Composable() (RowScope.() -> Unit) {
//    Row(){
//        if (loadingStatus == LoadingStatus.DONE || loadingStatus == LoadingStatus.ERROR) {
//            IconButton(
//                onClick = forceReload,
////                    modifier = Modifier.align(Alignment.End)
//            ) {
//                Icon(Icons.Default.Refresh, contentDescription = "Reload quotes")
//            }
//        }
//    }
//
//}
