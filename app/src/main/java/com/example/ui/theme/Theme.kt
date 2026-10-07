package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = DevilCrimson,
    onPrimary = SinWhite,
    primaryContainer = DevilBlood,
    onPrimaryContainer = SinWhite,
    secondary = DevilGold,
    onSecondary = SinBlack,
    secondaryContainer = SinSurfaceVariant,
    onSecondaryContainer = DevilGold,
    tertiary = DevilAmber,
    onTertiary = SinBlack,
    background = SinBlack,
    onBackground = SinWhite,
    surface = SinSurface,
    onSurface = SinWhite,
    surfaceVariant = SinSurfaceVariant,
    onSurfaceVariant = SinGrey,
    outline = SinBorder,
    outlineVariant = SinDarkGrey
)

@Composable
fun DevilsDeckTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = DevilsTypography,
        content = content
    )
}
