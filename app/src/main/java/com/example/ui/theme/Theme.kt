package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkNavyColorScheme = darkColorScheme(
    primary = EmeraldPrimary,
    onPrimary = DeepNavy,
    primaryContainer = EmeraldSurface,
    onPrimaryContainer = EmeraldLight,
    inversePrimary = EmeraldDark,

    secondary = EmeraldLight,
    onSecondary = DeepNavy,
    secondaryContainer = SurfaceNavy,
    onSecondaryContainer = TextPrimary,

    tertiary = Color(0xFF38BDF8), // Subtle cyan secondary accent
    onTertiary = DeepNavy,

    background = DeepNavy,
    onBackground = TextPrimary,

    surface = DarkNavy,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceNavy,
    onSurfaceVariant = TextSecondary,

    outline = NavyBorder,
    outlineVariant = NavyBorderLight,

    error = StatusError,
    onError = Color.White
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true, // Force premium dark navy as requested by the branding specification
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkNavyColorScheme,
        typography = Typography,
        content = content
    )
}
