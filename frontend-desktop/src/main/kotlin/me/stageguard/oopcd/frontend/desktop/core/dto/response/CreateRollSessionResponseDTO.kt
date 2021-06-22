package me.stageguard.oopcd.frontend.desktop.core.dto.response

import kotlinx.serialization.Serializable

@Serializable
data class CreateRollSessionResponseDTO(
    val sessionKey: String
)