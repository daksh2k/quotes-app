package com.example.quotesapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.graphics.Color

val Purple200 = Color(0xFFBB86FC)
val Purple500 = Color(0xFF6200EE)
val Purple700 = Color(0xFF3700B3)
val Teal200 = Color(0xFF03DAC5)

val pastelColors = mapOf(
    "Charcoal" to Color(0xFF264653),
    "Persian Green" to Color(0xFF2a9d8f),
    "Maize Crayola" to Color(0xFFe9c46a),
    "Sandy Brown" to Color(0xFFf4a261),
    "Burnt Sienna" to Color(0xFFe76f51),
    "Teal 200" to Teal200,
    "Purple 500" to Purple200
)

object AnimatingColor{
    val lightColors = pastelColors.values

    val darkColors = pastelColors.values
}