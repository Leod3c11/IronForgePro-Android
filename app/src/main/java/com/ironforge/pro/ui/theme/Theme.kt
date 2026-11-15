package com.ironforge.pro.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val HyperOsDarkScheme = darkColorScheme(
    primary = HyperOrange,
    onPrimary = TextPrimary,
    primaryContainer = HyperTileElevated,
    onPrimaryContainer = HyperOrangeGlow,
    secondary = HyperCyan,
    onSecondary = HyperBgDark,
    secondaryContainer = HyperTileElevated,
    onSecondaryContainer = HyperCyan,
    tertiary = HyperVolt,
    onTertiary = HyperBgDark,
    background = HyperBgDark,
    onBackground = TextPrimary,
    surface = HyperCardDark,
    onSurface = TextPrimary,
    surfaceVariant = HyperTileElevated,
    onSurfaceVariant = TextSecondary,
    outline = HyperHairline,
    error = HyperCrimson,
    onError = TextPrimary
)

@Composable
fun IronForgeTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = HyperOsDarkScheme,
        typography = IronTypography,
        content = content
    )
}
