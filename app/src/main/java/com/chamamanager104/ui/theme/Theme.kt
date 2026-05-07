package com.chamamanager104.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Color(0xFF0C5F46),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFD8F0E7),
    onPrimaryContainer = Color(0xFF07251C),
    secondary = Color(0xFFB87920),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFF7E3C2),
    onSecondaryContainer = Color(0xFF503608),
    tertiary = Color(0xFF0C5471),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFD3E8F3),
    onTertiaryContainer = Color(0xFF0B2530),
    background = Color(0xFFF4EFE6),
    surface = Color(0xFFFFFBF5),
    surfaceVariant = Color(0xFFE6E1D8),
    onSurface = Color(0xFF1E211D),
    onSurfaceVariant = Color(0xFF5B645C),
    outline = Color(0xFFB7B1A6)
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF58D0AE),
    onPrimary = Color(0xFF002116),
    primaryContainer = Color(0xFF0C5F46),
    secondary = Color(0xFFF2B24A),
    tertiary = Color(0xFF8CD1F0),
    background = Color(0xFF111412),
    surface = Color(0xFF171A18),
    onSurfaceVariant = Color(0xFFC1C8C2)
)

@Composable
fun ChamaManagerTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (isSystemInDarkTheme()) DarkColors else LightColors,
        typography = ChamaManagerTypography,
        content = content
    )
}
