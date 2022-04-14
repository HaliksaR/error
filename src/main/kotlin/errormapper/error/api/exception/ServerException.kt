package errormapper.error.api.exception

import errormapper.error.api.deprecated.ErrorCode
import errormapper.error.api.deprecated.HasErrorCode
import java.io.IOException

// ошибки с сервера, не коды из тела!
sealed class ServerException(
    override val message: String,
    override val errorCode: ErrorCode,
) : IOException(message), HasErrorCode

// HTTP_BAD_METHOD = 405
class UnsupportedMethodException(override val message: String) : ServerException(message, ErrorCode.UnsupportedMethod)

// HTTP_CONFLICT = 409
class ServerInnerException(override val message: String) : ServerException(message, ErrorCode.Inner)

// HTTP_NOT_ACCEPTABLE = 406
class UnsupportedApiException(override val message: String) : ServerException(message, ErrorCode.UnsupportedApi)

/// не задокументировано
// HTTP_NO_CONTENT = 204
class NoContentException(override val message: String) : ServerException(message, ErrorCode.NoContent)

// HTTP_GATEWAY_TIMEOUT = 504
class ServiceTimeoutException(override val message: String) : ServerException(message, ErrorCode.Timeout)

// else
class UnknownServerException(override val message: String) : ServerException(message, ErrorCode.Unknown)
/// не задокументировано