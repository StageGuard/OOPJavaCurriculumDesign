package me.stageguard.oopcd.frontend.desktop;

import androidx.compose.desktop.Window
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Typography
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.jetbrains.rememberRootComponent
import me.stageguard.oopcd.frontend.desktop.config.fontFamily
import me.stageguard.oopcd.frontend.desktop.config.lightTheme

fun main() = Window(
    title = "Roll System"
) {
    MaterialTheme(
        colors = lightTheme,
        typography = Typography(defaultFontFamily = fontFamily)
    ) {
        Surface(modifier = Modifier.fillMaxSize()) {
            rememberRootComponent(factory = {
                NavigationHostComponent(it)
            }).render()
        }
    }
}