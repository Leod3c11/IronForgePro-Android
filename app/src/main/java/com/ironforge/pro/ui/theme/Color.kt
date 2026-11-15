package com.ironforge.pro.ui.theme

import androidx.compose.ui.graphics.Color

// Semantic Status Colors (High contrast on both Dark/AMOLED & Light)
val HyperEmerald = Color(0xFF00C853)
val HyperEmeraldBright = Color(0xFF00E676)
val HyperAmber = Color(0xFFFFAB00)
val HyperCrimson = Color(0xFFFF3D57)
val HyperViolet = Color(0xFF8C52FF)

enum class ThemeSurfaceStyle(val label: String) {
    AMOLED("Preto AMOLED"),
    LIGHT("Tons Claros"),
    DARK("Dark Titanium")
}

enum class AppThemePreset(
    val id: String,
    val title: String,
    val subtitle: String,
    val style: ThemeSurfaceStyle,
    val isLight: Boolean,
    val bgHex: Long,
    val cardHex: Long,
    val elevatedHex: Long,
    val hairlineHex: Long,
    val primaryHex: Long,
    val secondaryHex: Long,
    val tertiaryHex: Long,
    val isDynamicWallpaper: Boolean = false
) {
    // ==================== PRETO AMOLED (#000000) ====================
    AMOLED_SOLAR_ORANGE(
        id = "amoled_solar_orange",
        title = "AMOLED Solar Orange",
        subtitle = "Preto Puro #000000 • Economia de bateria + Laranja HyperOS",
        style = ThemeSurfaceStyle.AMOLED,
        isLight = false,
        bgHex = 0xFF000000,
        cardHex = 0xFF0B0D11,
        elevatedHex = 0xFF141820,
        hairlineHex = 0xFF232936,
        primaryHex = 0xFFFF6900,
        secondaryHex = 0xFF00D2FF,
        tertiaryHex = 0xFFB8FF00
    ),
    AMOLED_CYBER_VOLT(
        id = "amoled_cyber_volt",
        title = "AMOLED Cyber Volt",
        subtitle = "Preto Puro #000000 • Lima Elétrico & Ciano Néon",
        style = ThemeSurfaceStyle.AMOLED,
        isLight = false,
        bgHex = 0xFF000000,
        cardHex = 0xFF0A0D0E,
        elevatedHex = 0xFF13191C,
        hairlineHex = 0xFF222C30,
        primaryHex = 0xFFC6FF00,
        secondaryHex = 0xFF00E5FF,
        tertiaryHex = 0xFF00E676
    ),
    AMOLED_IRIS_VIOLET(
        id = "amoled_iris_violet",
        title = "AMOLED Iris Violet",
        subtitle = "Preto Puro #000000 • Ametista & Magenta Expressive",
        style = ThemeSurfaceStyle.AMOLED,
        isLight = false,
        bgHex = 0xFF000000,
        cardHex = 0xFF0C0A12,
        elevatedHex = 0xFF171324,
        hairlineHex = 0xFF29223D,
        primaryHex = 0xFFB388FF,
        secondaryHex = 0xFFFF80AB,
        tertiaryHex = 0xFF64FFDA
    ),
    AMOLED_EMERALD_PRO(
        id = "amoled_emerald_pro",
        title = "AMOLED Bio-Emerald",
        subtitle = "Preto Puro #000000 • Verde Esmeralda & Turquesa",
        style = ThemeSurfaceStyle.AMOLED,
        isLight = false,
        bgHex = 0xFF000000,
        cardHex = 0xFF090E0C,
        elevatedHex = 0xFF111C18,
        hairlineHex = 0xFF1E332B,
        primaryHex = 0xFF00E676,
        secondaryHex = 0xFF1DE9B6,
        tertiaryHex = 0xFFFFEA00
    ),

    // ==================== TONS CLAROS (LIGHT MODE) ====================
    LIGHT_HYPER_SOLAR(
        id = "light_hyper_solar",
        title = "HyperOS Claro Solar",
        subtitle = "Fundo Porcelana Clara • Cartões Brancos + Laranja",
        style = ThemeSurfaceStyle.LIGHT,
        isLight = true,
        bgHex = 0xFFF3F5F9,
        cardHex = 0xFFFFFFFF,
        elevatedHex = 0xFFE8ECF4,
        hairlineHex = 0xFFD4DCE8,
        primaryHex = 0xFFFF6900,
        secondaryHex = 0xFF0088CC,
        tertiaryHex = 0xFF00A859
    ),
    LIGHT_MINT_SAGE(
        id = "light_mint_sage",
        title = "Android 17 Claro Sálvia",
        subtitle = "Tons Claros Naturais • Verde Floresta & Água-Marinha",
        style = ThemeSurfaceStyle.LIGHT,
        isLight = true,
        bgHex = 0xFFF0F5F2,
        cardHex = 0xFFFFFFFF,
        elevatedHex = 0xFFE2ECE6,
        hairlineHex = 0xFFC8DBD0,
        primaryHex = 0xFF00875A,
        secondaryHex = 0xFF00838F,
        tertiaryHex = 0xFFE65100
    ),
    LIGHT_LAVENDER_SILK(
        id = "light_lavender_silk",
        title = "Android 17 Claro Lavanda",
        subtitle = "Tons Claros Suaves • Íris Material You & Rosa",
        style = ThemeSurfaceStyle.LIGHT,
        isLight = true,
        bgHex = 0xFFF4F1FA,
        cardHex = 0xFFFFFFFF,
        elevatedHex = 0xFFE9E2F5,
        hairlineHex = 0xFFD5C9EB,
        primaryHex = 0xFF6750A4,
        secondaryHex = 0xFFC2185B,
        tertiaryHex = 0xFF00897B
    ),
    LIGHT_COBALT_ICE(
        id = "light_cobalt_ice",
        title = "Android 17 Claro Safira",
        subtitle = "Branco Glacial • Azul Cobalto & Ciano Profundo",
        style = ThemeSurfaceStyle.LIGHT,
        isLight = true,
        bgHex = 0xFFF0F4FA,
        cardHex = 0xFFFFFFFF,
        elevatedHex = 0xFFE1E9F6,
        hairlineHex = 0xFFC7D5EC,
        primaryHex = 0xFF1565C0,
        secondaryHex = 0xFF00838F,
        tertiaryHex = 0xFF6A1B9A
    ),

    // ==================== DARK TITANIUM & DINÂMICO ====================
    HYPER_ORANGE(
        id = "hyper_orange",
        title = "HyperOS Titanium Dark",
        subtitle = "Assinatura Xiaomi HyperOS • Grafite Escuro & Laranja",
        style = ThemeSurfaceStyle.DARK,
        isLight = false,
        bgHex = 0xFF0B0E14,
        cardHex = 0xFF151A24,
        elevatedHex = 0xFF1D2433,
        hairlineHex = 0xFF2B3447,
        primaryHex = 0xFFFF6900,
        secondaryHex = 0xFF00D2FF,
        tertiaryHex = 0xFFB8FF00
    ),
    ANDROID_17_DYNAMIC(
        id = "android_17_dynamic",
        title = "Android 17 Wallpaper Dinâmico",
        subtitle = "Material You • Extrai cores do papel de parede",
        style = ThemeSurfaceStyle.DARK,
        isLight = false,
        bgHex = 0xFF0B0E14,
        cardHex = 0xFF151A24,
        elevatedHex = 0xFF1D2433,
        hairlineHex = 0xFF2B3447,
        primaryHex = 0xFF8AB4F8,
        secondaryHex = 0xFF81C995,
        tertiaryHex = 0xFFFDD663,
        isDynamicWallpaper = true
    );

    companion object {
        fun fromId(id: String): AppThemePreset =
            entries.find { it.id == id } ?: AMOLED_SOLAR_ORANGE
    }
}
