package com.ironforge.pro.ui.theme

import androidx.compose.ui.graphics.Color

val HyperBgDark = Color(0xFF0B0E14)
val HyperCardDark = Color(0xFF151A24)
val HyperTileElevated = Color(0xFF1D2433)
val HyperGlassOverlay = Color(0xD9161C28)
val HyperHairline = Color(0xFF2B3447)
val HyperSpecularTop = Color(0x33FFFFFF)

val HyperEmerald = Color(0xFF00E676)
val HyperAmber = Color(0xFFFFB800)
val HyperCrimson = Color(0xFFFF4757)
val HyperViolet = Color(0xFF9D6BFF)

val TextPrimary = Color(0xFFF5F8FF)
val TextSecondary = Color(0xFFA2B0C8)
val TextMuted = Color(0xFF687791)

enum class AppThemePreset(
    val id: String,
    val title: String,
    val subtitle: String,
    val primaryHex: Long,
    val secondaryHex: Long,
    val tertiaryHex: Long,
    val isDynamicWallpaper: Boolean = false
) {
    HYPER_ORANGE(
        id = "hyper_orange",
        title = "HyperOS Solar Orange",
        subtitle = "Assinatura Xiaomi HyperOS • Laranja & Ciano",
        primaryHex = 0xFFFF6900,
        secondaryHex = 0xFF00D2FF,
        tertiaryHex = 0xFFB8FF00
    ),
    ANDROID_17_DYNAMIC(
        id = "android_17_dynamic",
        title = "Android 17 Wallpaper Dinâmico",
        subtitle = "Material You • Adapta-se às cores do seu sistema",
        primaryHex = 0xFF8AB4F8,
        secondaryHex = 0xFF81C995,
        tertiaryHex = 0xFFFDD663,
        isDynamicWallpaper = true
    ),
    CYBER_VOLT(
        id = "cyber_volt",
        title = "Android 17 Cyber Volt",
        subtitle = "Material Expressive • Lima Elétrico & Turquesa",
        primaryHex = 0xFFC6FF00,
        secondaryHex = 0xFF00E5FF,
        tertiaryHex = 0xFF00E676
    ),
    IRIS_VIOLET(
        id = "iris_violet",
        title = "Android 17 Iris Violet",
        subtitle = "Material Expressive • Ametista & Rosa Néon",
        primaryHex = 0xFFB388FF,
        secondaryHex = 0xFFFF80AB,
        tertiaryHex = 0xFF64FFDA
    ),
    EMERALD_MINT(
        id = "emerald_mint",
        title = "Android 17 Bio-Mint",
        subtitle = "Material Expressive • Esmeralda & Água-Marinha",
        primaryHex = 0xFF00E676,
        secondaryHex = 0xFF1DE9B6,
        tertiaryHex = 0xFFFFEA00
    ),
    CRIMSON_TITANIUM(
        id = "crimson_titanium",
        title = "Android 17 Crimson Pro",
        subtitle = "Material Expressive • Rubi Intenso & Âmbar",
        primaryHex = 0xFFFF5252,
        secondaryHex = 0xFFFFAB40,
        tertiaryHex = 0xFF40C4FF
    ),
    COBALT_OCEAN(
        id = "cobalt_ocean",
        title = "Android 17 Cobalt Wave",
        subtitle = "Material Expressive • Safira & Ciano Glacial",
        primaryHex = 0xFF448AFF,
        secondaryHex = 0xFF18FFFF,
        tertiaryHex = 0xFFB388FF
    );

    companion object {
        fun fromId(id: String): AppThemePreset =
            entries.find { it.id == id } ?: HYPER_ORANGE
    }
}
