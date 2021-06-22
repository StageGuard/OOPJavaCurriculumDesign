package me.stageguard.oopcd.frontend.desktop.core

object SettingField {
    val layer: Int
        get() = 4
    val ratio: List<Double>
        get() = mutableListOf(0.0, 0.2, 0.5, 0.7)
    val transferCount: List<Int>
        get() = mutableListOf(5, 4, 3, 3)
    val rollAlsoFromNextLayer: Boolean
        get() = false
}