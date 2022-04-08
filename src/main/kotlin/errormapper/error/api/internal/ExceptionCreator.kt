package errormapper.error.api.internal

import errormapper.error.api.exception.*
import kotlinx.serialization.json.Json
import okhttp3.Request
import okhttp3.Response
import sun.net.www.protocol.http.HttpURLConnection
import java.io.IOException
import java.net.ConnectException
import java.net.NoRouteToHostException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

private val DOCUMENTED_HTTP_CODES = setOf(
    HttpURLConnection.HTTP_INTERNAL_ERROR,
    HttpURLConnection.HTTP_UNAVAILABLE,
    HttpURLConnection.HTTP_BAD_REQUEST,
    HttpURLConnection.HTTP_NOT_FOUND,
    HttpURLConnection.HTTP_UNAUTHORIZED
)

internal fun createDomainException(
    request: Request,
    response: Response,
    serializer: Json,
    context: String
): DomainException? =
    if (response.code in DOCUMENTED_HTTP_CODES) {
        val mapper = request.errorMapper(context)
        val (code, message) = response.errorModel(serializer)
        mapper(code, message)
    } else {
        null
    }

internal fun createServerException(response: Response): ServerException =
    when (response.code) {
        HttpURLConnection.HTTP_BAD_METHOD -> UnsupportedMethodException(response.message)
        HttpURLConnection.HTTP_CONFLICT -> ServerInnerException(response.message)
        HttpURLConnection.HTTP_NOT_ACCEPTABLE -> UnsupportedApiException(response.message)

        // не задокументировано
        HttpURLConnection.HTTP_NO_CONTENT -> NoContentException(response.message)
        HttpURLConnection.HTTP_GATEWAY_TIMEOUT -> ServiceTimeoutException(response.message)
        else -> UnknownServerException(response.message)
    }

// проблемы локального характера. не задокументировано
internal fun createLocalNetworkException(exception: IOException, context: String): LocalNetworkException =
    when (exception) {
        is UnknownHostException,
        is ConnectException,
        is NoRouteToHostException -> NetworkConnectException(context.toString() + exception.message)

        is SocketTimeoutException -> NetworkTimeoutException(context.toString() + exception.message)

        else -> NetworkUnknownException(context.toString() + exception.message)
    }.apply { addSuppressed(exception) }