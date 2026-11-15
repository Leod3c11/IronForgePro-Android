package com.ironforge.pro.ui.theme

import android.os.Build
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.platform.LocalContext

@Immutable
data class IronAccentPalette(
    val isLight: Boolean,
    val primary: Color,
    val primaryGlow: Color,
    val onPrimaryAccent: Color,
    val secondary: Color,
    val tertiary: Color,
    val bg: Color,
    val card: Color,
    val tileElevated: Color,
    val glassOverlay: Color,
    val hairline: Color,
    val specularTop: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val textMuted: Color
)

val LocalIronPalette = staticCompositionLocalOf {
    IronAccentPalette(
        isLight = false,
        primary = Color(0xFFFF6900),
        primaryGlow = Color(0xFFFF9147),
        onPrimaryAccent = Color(0xFFFFFFFF),
        secondary = Color(0xFF00D2FF),
        tertiary = Color(0xFFB8FF00),
        bg = Color(0xFF000000),
        card = Color(0xFF0B0D11),
        tileElevated = Color(0xFF141820),
        glassOverlay = Color(0xE60B0D11),
        hairline = Color(0xFF232936),
        specularTop = Color(0x2EFFFFFF),
        textPrimary = Color(0xFFF5F8FF),
        textSecondary = Color(0xFFA2B0C8),
        textMuted = Color(0xFF687791)
    )
}

@Composable
fun IronForgeTheme(
    preset: AppThemePreset = AppThemePreset.AMOLED_SOLAR_ORANGE,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current

    val (colorScheme, palette) = if (preset.isDynamicWallpaper && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val dyn = dynamicDarkColorScheme(context)
        val onPrim = if (dyn.primary.luminance() > 0.45f) Color(0xFF080A0F) else Color(0xFFF5F8FF)
        val bgCol = Color(preset.bgHex)
        val cardCol = Color(preset.cardHex)
        val elevCol = Color(preset.elevatedHex)
        val lineCol = Color(preset.hairlineHex)
        val p = IronAccentPalette(
            isLight = false,
            primary = dyn.primary,
            primaryGlow = Color(
                red = (dyn.primary.red * 0.8f + 0.2f).coerceIn(0f, 1f),
                green = (dyn.primary.green * 0.8f + 0.2f).coerceIn(0f, 1f),
                blue = (dyn.primary.blue * 0.8f + 0.2f).coerceIn(0f, 1f)
            ),
            onPrimaryAccent = onPrim,
            secondary = dyn.secondary,
            tertiary = dyn.tertiary,
            bg = bgCol,
            card = cardCol,
            tileElevated = elevCol,
            glassOverlay = Color(0xE6121620),
            hairline = lineCol,
            specularTop = Color(0x2EFFFFFF),
            textPrimary = Color(0xFFF5F8FF),
            textSecondary = Color(0xFFA2B0C8),
            textMuted = Color(0xFF687791)
        )
        val scheme = dyn.copy(
            background = bgCol,
            surface = cardCol,
            surfaceVariant = elevCol,
            outline = lineCol,
            onPrimary = onPrim
        )
        scheme to p
    } else if (preset.isLight) {
        val primary = Color(preset.primaryHex)
        val secondary = Color(preset.secondaryHex)
        val tertiary = Color(preset.tertiaryHex)
        val bgCol = Color(preset.bgHex)
        val cardCol = Color(preset.cardHex)
        val elevCol = Color(preset.elevatedHex)
        val lineCol = Color(preset.hairlineHex)
        val onPrim = if (primary.luminance() > 0.55f) Color(0xFF0E131B) else Color(0xFFFFFFFF)
        val p = IronAccentPalette(
            isLight = true,
            primary = primary,
            primaryGlow = primary,
            onPrimaryAccent = onPrim,
            secondary = secondary,
            tertiary = tertiary,
            bg = bgCol,
            card = cardCol,
            tileElevated = elevCol,
            glassOverlay = Color(0xEBFFFFFF),
            hairline = lineCol,
            specularTop = Color(0xFFFFFFFF),
            textPrimary = Color(0xFF101622),
            textSecondary = Color(0xFF475569),
            textMuted = Color(0xFF7B879A)
        )
        val scheme = lightColorScheme(
            primary = primary,
            onPrimary = onPrim,
            primaryContainer = elevCol,
            onPrimaryContainer = primary,
            secondary = secondary,
            onSecondary = Color(0xFFFFFFFF),
            secondaryContainer = elevCol,
            onSecondaryContainer = secondary,
            tertiary = tertiary,
            onTertiary = Color(0xFFFFFFFF),
            background = bgCol,
            onBackground = p.textPrimary,
            surface = cardCol,
            onSurface = p.textPrimary,
            surfaceVariant = elevCol,
            onSurfaceVariant = p.textSecondary,
            outline = lineCol,
            error = HyperCrimson,
            onError = Color(0xFFFFFFFF)
        )
        scheme to p
    } else {
        val primary = Color(preset.primaryHex)
        val secondary = Color(preset.secondaryHex)
        val tertiary = Color(preset.tertiaryHex)
        val bgCol = Color(preset.bgHex)
        val cardCol = Color(preset.cardHex)
        val elevCol = Color(preset.elevatedHex)
        val lineCol = Color(preset.hairlineHex)
        val onPrim = if (primary.luminance() > 0.48f) Color(0xFF080A0F) else Color(0xFFF5F8FF)
        val glow = Color(
            red = (primary.red * 0.78f + 0.22f).coerceIn(0f, 1f),
            green = (primary.green * 0.78f + 0.22f).coerceIn(0f, 1f),
            blue = (primary.blue * 0.78f + 0.22f).coerceIn(0f, 1f)
        )
        val p = IronAccentPalette(
            isLight = false,
            primary = primary,
            primaryGlow = glow,
            onPrimaryAccent = onPrim,
            secondary = secondary,
            tertiary = tertiary,
            bg = bgCol,
            card = cardCol,
            tileElevated = elevCol,
            glassOverlay = Color(0xE60D1017),
            hairline = lineCol,
            specularTop = Color(0x2EFFFFFF),
            textPrimary = Color(0xFFF5F8FF),
            textSecondary = Color(0xFFA2B0C8),
            textMuted = Color(0xFF687791)
        )
        val scheme = darkColorScheme(
            primary = primary,
            onPrimary = onPrim,
            primaryContainer = elevCol,
            onPrimaryContainer = glow,
            secondary = secondary,
            onSecondary = bgCol,
            secondaryContainer = elevCol,
            onSecondaryContainer = secondary,
            tertiary = tertiary,
            onTertiary = bgCol,
            background = bgCol,
            onBackground = p.textPrimary,
            surface = cardCol,
            onSurface = p.textPrimary,
            surfaceVariant = elevCol,
            onSurfaceVariant = p.textSecondary,
            outline = lineCol,
            error = HyperCrimson,
            onError = p.textPrimary
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
