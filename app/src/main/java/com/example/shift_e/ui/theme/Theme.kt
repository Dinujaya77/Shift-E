package com.example.shift_e.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary        = GreenPrimary,
    onPrimary      = Color.White,
    secondary      = TealDark,
    background     = CreamBackground,
    surface        = Color.White,
    onSurface      = Color.Black,
    outline        = GrayLight
)

private val DarkColors = darkColorScheme(
    primary        = GreenPrimary,
    onPrimary      = Color.Black,
    secondary      = TealDark,
    background     = Color.Black,
    surface        = GrayDark,
    onSurface      = Color.White,
    outline        = GrayDark
)

@Composable
fun Shift_ETheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColors else LightColors

    MaterialTheme(
        colorScheme  = colors,
        typography   = ShiftETypography,
        shapes       = Shapes(),    // use default shapes or override if you like
        content      = content
    )
}
