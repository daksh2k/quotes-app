package com.example.quotesapp.ui.components


import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.OpenWith
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.ViewList
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.quotesapp.LoadingStatus
import com.example.quotesapp.R
import com.example.quotesapp.ui.theme.Purple200
import com.example.quotesapp.ui.theme.QuotesAppTheme

/**
 * Basic [TopAppBar] wrapper.
 */
@Composable
fun QuotesTopAppBar(
    forceReload: () -> Unit,
    loadingStatus: LoadingStatus,
    themeColor: Color,
    listLayout: Boolean,
    onListClick: () -> Unit
) {
    TopAppBar(
        title = { Text(text = stringResource(id = R.string.app_name)) },
        modifier = Modifier.alpha(1f),
        actions = {
            TopActions(
                loadingStatus = loadingStatus,
                forceReload = forceReload,
                listLayout = listLayout,
                onListClick = onListClick
            )
        },
        backgroundColor = themeColor,
        contentColor = MaterialTheme.colors.onPrimary,
        elevation = 0.dp
    )

}

@Composable
fun TopActions(
    loadingStatus: LoadingStatus,
    forceReload: () -> Unit,
    listLayout: Boolean,
    onListClick: () -> Unit
) {
    if (loadingStatus == LoadingStatus.DONE || loadingStatus == LoadingStatus.ERROR) {
        IconButton(
            onClick = forceReload,
            modifier = Modifier
        ) {
            Icon(Icons.Default.Refresh, contentDescription = "Reload quotes")
        }
    }
    IconButton(
        onClick = onListClick,
        modifier = Modifier
    ) {
        if (listLayout) {
            Icon(Icons.Default.OpenWith, contentDescription = "Switch to Single View")
        } else {
            Icon(Icons.Default.ViewList, contentDescription = "Switch to List Layout")
        }
    }

}

@Composable
@Preview(showBackground = true)
fun PreviewTopBar() {
    QuotesAppTheme {
        QuotesTopAppBar(
            forceReload = {},
            loadingStatus = LoadingStatus.DONE,
            themeColor = Purple200,
            listLayout = false,
            {}
        )
    }
}
