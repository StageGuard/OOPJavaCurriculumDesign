package me.stageguard.oopcd.frontend.desktop.core

import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.utils.io.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import me.stageguard.oopcd.frontend.desktop.Either
import me.stageguard.oopcd.frontend.desktop.core.dto.request.CreateRollSessionRequestDTO
import me.stageguard.oopcd.frontend.desktop.core.dto.request.GetStudentsRequestFilterDTO
import me.stageguard.oopcd.frontend.desktop.core.dto.request.SettingFieldDTO
import me.stageguard.oopcd.frontend.desktop.core.dto.response.*
import java.util.regex.Pattern

@Suppress("DuplicatedCode")
object RollManager {
    private val client = HttpClient(OkHttp)
    private const val AUTH_KEY = "114514_1919810"
    private const val BASE_URL = "http://localhost:8088"

    private val json = Json { ignoreUnknownKeys = true }

    @Suppress("RemoveExplicitTypeArguments")
    suspend fun getStudents(
        limit: Int = 1000,
        filter: List<GetStudentsRequestFilterDTO>
    ): Either<GetStudentsResponseDTO, ErrorResponseDTO> = try {
        if (filter.isEmpty()) {
            client.get<HttpStatement> {
                url("$BASE_URL/v1/getStudents?limit=$limit")
                header("Authorization", AUTH_KEY)
            }
        } else {
            client.post<HttpStatement> {
                url("$BASE_URL/v1/getStudents")
                header("Authorization", AUTH_KEY)
                contentType(ContentType.Application.Json)
                body = """
                    {"limit": $limit, "filter": ${filter.map { json.encodeToString(it) }}}
                """.trimIndent()
            }
        }.execute { response ->
            val result = response.content.readUTF8Line() ?: """
                {"error": "Empty content."}
            """.trimIndent()
            if (Pattern.compile("error").matcher(result).find()) {
                Either.Right(json.decodeFromString(result))
            } else {
                Either.Left(json.decodeFromString(result))
            }
        }
    } catch (ex: Exception) {
        Either.Right(ErrorResponseDTO(ex.toString()))
    }

    suspend fun createRollSession(): Either<CreateRollSessionResponseDTO, ErrorResponseDTO> = try {
        client.post<HttpStatement> {
            url("$BASE_URL/v1/createRollSession")
            header("Authorization", AUTH_KEY)
            contentType(ContentType.Application.Json)
            body = Json.encodeToString(
                CreateRollSessionRequestDTO(
                    SettingFieldDTO(
                        layer = SettingField.layer,
                        ratio = SettingField.ratio,
                        transferCount = SettingField.transferCount,
                        rollAlsoFromNextLayer = SettingField.rollAlsoFromNextLayer
                    )
                )
            )
        }.execute { response ->
            val result = response.content.readUTF8Line() ?: """
                {"error": "Empty content."}
            """.trimIndent()
            if (Pattern.compile("error").matcher(result).find()) {
                Either.Right(json.decodeFromString(result))
            } else {
                Either.Left(json.decodeFromString(result))
            }
        }
    } catch (ex: Exception) {
        Either.Right(ErrorResponseDTO(ex.toString()))
    }

    suspend fun roll(
        sessionKey: String
    ): Either<RollResponseDTO, ErrorResponseDTO> = try {
        client.post<HttpStatement> {
            url("$BASE_URL/v1/roll")
            header("Authorization", AUTH_KEY)
            contentType(ContentType.Application.Json)
            body = """
                {"sessionKey": "$sessionKey"}
            """.trimIndent()
        }.execute { response ->
            val result = response.content.readUTF8Line() ?: """
                {"error": "Empty content."}
            """.trimIndent()
            if (Pattern.compile("error").matcher(result).find()) {
                Either.Right(json.decodeFromString(result))
            } else {
                Either.Left(json.decodeFromString(result))
            }
        }
    } catch (ex: Exception) {
        Either.Right(ErrorResponseDTO(ex.toString()))
    }

    suspend fun answer(
        sessionKey: String,
        isRight: Boolean
    ): Either<AnswerResponseDTO, ErrorResponseDTO> = try {
        client.post<HttpStatement> {
            url("$BASE_URL/v1/answer")
            header("Authorization", AUTH_KEY)
            contentType(ContentType.Application.Json)
            body = """
                {"sessionKey": "$sessionKey", "isRight": $isRight}
            """.trimIndent()
        }.execute { response ->
            val result = response.content.readUTF8Line() ?: """
                {"error": "Empty content."}
            """.trimIndent()
            if (Pattern.compile("error").matcher(result).find()) {
                Either.Right(json.decodeFromString(result))
            } else {
                Either.Left(json.decodeFromString(result))
            }
        }
    } catch (ex: Exception) {
        Either.Right(ErrorResponseDTO(ex.toString()))
    }

}