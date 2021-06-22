package me.stageguard.oopcd.frontend.desktop.core.dto.request

import kotlinx.serialization.Serializable

@Serializable
data class GetStudentsRequestFilterDTO(
    val op: String,
    val key: String,
    val value: String
)