package com.ironforge.pro.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val IronDarkColorScheme = darkColorScheme(
    primary = ElectricVolt,
    onPrimary = ObsidianDeep,
    primaryContainer = SteelElevated,
    onPrimaryContainer = ElectricVolt,
    secondary = CyanPulse,
    onSecondary = ObsidianDeep,
    secondaryContainer = SteelElevated,
    onSecondaryContainer = CyanPulse,
    tertiary = EmeraldPR,
    onTertiary = ObsidianDeep,
    background = ObsidianDeep,
    onBackground = TextPrimary,
    surface = CarbonSurface,
    onSurface = TextPrimary,
    surfaceVariant = SteelElevated,
    onSurfaceVariant = TextSecondary,
    outline = GraphiteBorder,
    error = CrimsonPeak,
    onError = TextPrimary
)

@Composable
fun IronForgeTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = IronDarkColorScheme,
        typography = IronTypography,
        content = content
    )
}
