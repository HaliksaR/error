package errormapper.error.api.internal

import errormapper.error.api.exception.ServerInnerException
import kotlinx.serialization.SerializationException
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import okhttp3.Response

@kotlinx.serialization.Serializable
internal data class ErrorModel(val code: Int, val message: String)

internal fun Response.errorModel(serializer: Json): ErrorModel {
    try {
        val json = body?.string() ?: throw ServerInnerException("Incorrect error body in ${request.url}")
        return serializer.decodeFromString(json)
    } catch (_: SerializationException) {
        throw ServerInnerException("Incorrect error body in ${request.url}")
    }
}
