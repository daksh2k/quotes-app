package com.example.quotesapp.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.quotesapp.ui.theme.Purple200

/**
 * A simple Lazy row which shows tags.
 * Uses [TagButton] to show active and inactive tags.
 * @param tags Inactive tags
 * @param activeTags Active tags
 * @param onTagClick What should happen on tag click.
 */
@Composable
fun TagBarRow(
    modifier: Modifier = Modifier,
    themeColor: Color,
    tags: List<String>,
    activeTags: List<String>,
    onTagClick: (text: String) -> Unit
) {
    val contentColorForActiveButton =
        if (!isSystemInDarkTheme()) darkenColor(themeColor) else brightenColor(themeColor)
    LazyRow(modifier = modifier.padding(8.dp)) {
        items(activeTags) { tag ->
            TagButton(
                border = BorderStroke(1.dp, contentColorForActiveButton),
                butColors = ButtonDefaults.buttonColors(
                    backgroundColor = if (isSystemInDarkTheme()) darkenColor(
                        themeColor,
                        0.4f
                    ) else brightenColor(themeColor, 0.4f),
                    contentColor = contentColorForActiveButton
                ),
                text = tag,
                onTagClick = onTagClick
            )
        }

        items(tags) { tag ->
            TagButton(
                butColors = ButtonDefaults.buttonColors(
                    backgroundColor = themeColor.copy(0.2f),
                    contentColor = themeColor
                ),
                text = tag,
                onTagClick = onTagClick
            )
        }
    }
}

/**
 * A simple Lazy row which shows tags.
 * Uses [TagButton] to show active and inactive tags.
 * @param tags Inactive tags
 * @param activeTags Active tags
 * @param onTagClick What should happen on tag click.
 */
@Composable
fun HeaderTagBarRow(
    modifier: Modifier = Modifier,
    themeColor: Color,
    tags: List<String>,
    activeTags: List<String>,
    onTagClick: (text: String) -> Unit
) {
    LazyRow(
        modifier = modifier
            .padding(8.dp)
            .background(MaterialTheme.colors.onPrimary)
            .fillMaxWidth()
    ) {
        items(activeTags) { tag ->
            TagButton(
                modifier = Modifier
                    .padding(4.dp)
                    .clip(RoundedCornerShape(45)),
                butColors = ButtonDefaults.buttonColors(
                    backgroundColor = if (isSystemInDarkTheme()) darkenColor(
                        themeColor,
                        0.3f
                    ) else brightenColor(themeColor, 0.3f),
                    contentColor = if (!isSystemInDarkTheme()) darkenColor(themeColor) else brightenColor(
                        themeColor
                    )
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
                butColors = ButtonDefaults.buttonColors(
                    backgroundColor = themeColor.copy(0.1f),
                    contentColor = themeColor
                ),
                text = tag,
                onTagClick = onTagClick
            )
        }
    }
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