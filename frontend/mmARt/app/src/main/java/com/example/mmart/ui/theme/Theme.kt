package com.example.mmart.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val Palette = lightColors(
    primary = Main_blue,
//    primary = Dark_gray,
    background = Color.White,
    /* Other default colors to override
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@Composable
fun MmARtTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colors = Palette,
        typography = mainTypography,
        shapes = Shapes,
        content = content
    )
}