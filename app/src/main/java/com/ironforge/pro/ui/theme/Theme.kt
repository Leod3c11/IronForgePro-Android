package com.ironforge.pro.ui.theme

import android.os.Build
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.platform.LocalContext

@Immutable
data class IronAccentPalette(
    val primary: Color,
    val primaryGlow: Color,
    val onPrimaryAccent: Color,
    val secondary: Color,
    val tertiary: Color,
    val bgDark: Color,
    val cardDark: Color,
    val tileElevated: Color,
    val hairline: Color
)

val LocalIronPalette = staticCompositionLocalOf {
    IronAccentPalette(
        primary = Color(0xFFFF6900),
        primaryGlow = Color(0xFFFF9147),
        onPrimaryAccent = Color(0xFFF5F8FF),
        secondary = Color(0xFF00D2FF),
        tertiary = Color(0xFFB8FF00),
        bgDark = HyperBgDark,
        cardDark = HyperCardDark,
        tileElevated = HyperTileElevated,
        hairline = HyperHairline
    )
}

@Composable
fun IronForgeTheme(
    preset: AppThemePreset = AppThemePreset.HYPER_ORANGE,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current

    val (colorScheme, palette) = if (preset.isDynamicWallpaper && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val dyn = dynamicDarkColorScheme(context)
        val onPrim = if (dyn.primary.luminance() > 0.45f) Color(0xFF0B0E14) else Color(0xFFF5F8FF)
        val p = IronAccentPalette(
            primary = dyn.primary,
            primaryGlow = Color(
                red = (dyn.primary.red * 0.8f + 0.2f).coerceIn(0f, 1f),
                green = (dyn.primary.green * 0.8f + 0.2f).coerceIn(0f, 1f),
                blue = (dyn.primary.blue * 0.8f + 0.2f).coerceIn(0f, 1f)
            ),
            onPrimaryAccent = onPrim,
            secondary = dyn.secondary,
            tertiary = dyn.tertiary,
            bgDark = HyperBgDark,
            cardDark = HyperCardDark,
            tileElevated = HyperTileElevated,
            hairline = HyperHairline
        )
        val scheme = dyn.copy(
            background = HyperBgDark,
            surface = HyperCardDark,
            surfaceVariant = HyperTileElevated,
            outline = HyperHairline,
            onPrimary = onPrim
        )
        scheme to p
    } else {
        val primary = Color(preset.primaryHex)
        val secondary = Color(preset.secondaryHex)
        val tertiary = Color(preset.tertiaryHex)
        val onPrim = if (primary.luminance() > 0.48f) Color(0xFF0B0E14) else Color(0xFFF5F8FF)
        val glow = Color(
            red = (primary.red * 0.78f + 0.22f).coerceIn(0f, 1f),
            green = (primary.green * 0.78f + 0.22f).coerceIn(0f, 1f),
            blue = (primary.blue * 0.78f + 0.22f).coerceIn(0f, 1f)
        )
        val p = IronAccentPalette(
            primary = primary,
            primaryGlow = glow,
            onPrimaryAccent = onPrim,
            secondary = secondary,
            tertiary = tertiary,
            bgDark = HyperBgDark,
            cardDark = HyperCardDark,
            tileElevated = HyperTileElevated,
            hairline = HyperHairline
        )
        val scheme = darkColorScheme(
            primary = primary,
            onPrimary = onPrim,
            primaryContainer = HyperTileElevated,
            onPrimaryContainer = glow,
            secondary = secondary,
            onSecondary = HyperBgDark,
            secondaryContainer = HyperTileElevated,
            onSecondaryContainer = secondary,
            tertiary = tertiary,
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
        scheme to p
    }

    CompositionLocalProvider(LocalIronPalette provides palette) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = IronTypography,
            content = content
        )
    }
}
