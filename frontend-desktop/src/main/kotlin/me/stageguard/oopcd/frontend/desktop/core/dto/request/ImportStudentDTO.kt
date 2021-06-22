package me.stageguard.oopcd.frontend.desktop.core.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ImportStudentDTO(
    val name: String,
    val id: Long,
    @SerialName("class") val clazz: String
)

@Serializable
data class ImportStudentsDTO(
    val students: List<ImportStudentDTO>
)