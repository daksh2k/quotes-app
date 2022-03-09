package com.example.quotesapp.utils

import androidx.compose.ui.graphics.Color

fun getValidTags(tags: List<String>, activeTags: List<String>): List<String> {
    return tags
        .distinct()
        .filter { !activeTags.contains(it) && it.length < 12 }
        .take(5 - activeTags.size)
}

fun brightenColor(color: Color, alpha: Float = 1F): Color {
    return color.copy(
        alpha = alpha,
        red = (color.red * 1.3).toFloat().coerceAtMost(1f),
        green = (color.green * 1.3).toFloat().coerceAtMost(1f),
        blue = (color.blue * 1.3).toFloat().coerceAtMost(1f)
    )
}

fun darkenColor(color: Color, alpha: Float = 1f): Color {
    return color.copy(
        alpha = alpha,
        red = (color.red * .7).toFloat().coerceAtLeast(0f),
        green = (color.green * .7).toFloat().coerceAtLeast(0f),
        blue = (color.blue * .7).toFloat().coerceAtLeast(0f)
    )
}