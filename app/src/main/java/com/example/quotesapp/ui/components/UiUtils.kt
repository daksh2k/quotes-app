package com.example.quotesapp.ui.components

import android.content.Intent
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color


/**
 * Returns whether the lazy list is currently scrolling up.
 */
@Composable
fun LazyListState.isScrollingUp(): Boolean {
    var previousIndex by remember(this) { mutableStateOf(firstVisibleItemIndex) }
    var previousScrollOffset by remember(this) { mutableStateOf(firstVisibleItemScrollOffset) }
    return remember(this) {
        derivedStateOf {
            if (previousIndex != firstVisibleItemIndex) {
                previousIndex > firstVisibleItemIndex
            } else {
                previousScrollOffset - 20 >= firstVisibleItemScrollOffset
            }.also {
                previousIndex = firstVisibleItemIndex
                previousScrollOffset = firstVisibleItemScrollOffset + 20
            }
        }
    }.value
}

/**
 * Utility method to brighten a [Color].
 * @param color A [Color] to brighten.
 * @param alpha An optional value to change the alpha value of color.
 * @return [Color]
 */
fun brightenColor(color: Color, alpha: Float = 1F): Color {
    return color.copy(
        alpha = alpha,
        red = (color.red * 1.3).toFloat().coerceAtMost(1f),
        green = (color.green * 1.3).toFloat().coerceAtMost(1f),
        blue = (color.blue * 1.3).toFloat().coerceAtMost(1f)
    )
}

/**
 * Utility method to darken a [Color].
 * @param color A [Color] to darken.
 * @param alpha An optional value to change the alpha value of color.
 * @return [Color]
 */
fun darkenColor(color: Color, alpha: Float = 1f): Color {
    return color.copy(
        alpha = alpha,
        red = (color.red * .7).toFloat().coerceAtLeast(0f),
        green = (color.green * .7).toFloat().coerceAtLeast(0f),
        blue = (color.blue * .7).toFloat().coerceAtLeast(0f)
    )
}

/**
 * Create a simple share [Intent] from text.
 * @param text Text to specify in the intent.
 * @return An [Intent]
 */
fun createShareIntent(text: String): Intent {
    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, text)
        type = "text/plain"
    }
    return Intent.createChooser(sendIntent, null)
}