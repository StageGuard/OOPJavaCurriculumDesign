package me.stageguard.oopcd.frontend.desktop.core

object SettingField {
    var layer: Int = 4
    var ratio: List<Double> = mutableListOf(0.0, 0.2, 0.5, 0.7)
    var transferCount: List<Int> = mutableListOf(5, 4, 3, 3)
    var rollAlsoFromNextLayer: Boolean = false
}