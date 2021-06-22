package me.stageguard.oopcd.frontend.desktop.config

import androidx.compose.material.darkColors
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.platform.Font

var darkTheme = darkColors(
    primary = Color(48, 163, 230),
    secondary = Color(24, 25, 29),
    surface = Color(24, 25, 29),
    onPrimary = Color.White,
    onSecondary = Color.White
)

var fontFamily = FontFamily(Font("google_sans_regular.ttf"))