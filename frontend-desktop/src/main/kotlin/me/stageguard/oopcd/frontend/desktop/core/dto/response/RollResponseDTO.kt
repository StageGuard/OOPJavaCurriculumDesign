package me.stageguard.oopcd.frontend.desktop.core.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RollResponseDTO(
    val student: StudentInfoDTO
)

@Serializable
data class StudentInfoDTO(
    val id: Long,
    val name: String,
    @SerialName("class") val clazz: String,
    val totalAnswered: Int,
    val rightAnswered: Int
)