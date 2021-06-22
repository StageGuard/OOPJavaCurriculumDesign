package me.stageguard.oopcd.frontend.desktop.core.dto.request

import kotlinx.serialization.Serializable

@Serializable
data class CreateRollSessionRequestDTO(
    val config: SettingFieldDTO
)

@Serializable
data class SettingFieldDTO(
    val layer: Int,
    val ratio: List<Double>,
    val transferCount: List<Int>,
    val rollAlsoFromNextLayer: Boolean
)