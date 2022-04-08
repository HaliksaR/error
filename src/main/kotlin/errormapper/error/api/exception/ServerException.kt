package errormapper.error.api.exception

import java.io.IOException

// ошибки с сервера, не коды из тела!
sealed class ServerException(override val message: String) : IOException(message)

// HTTP_BAD_METHOD = 405
class UnsupportedMethodException internal constructor(override val message: String) : ServerException(message)

// HTTP_CONFLICT = 409
class ServerInnerException internal constructor(override val message: String) : ServerException(message)

// HTTP_NOT_ACCEPTABLE = 406
class UnsupportedApiException internal constructor(override val message: String) : ServerException(message)

/// не задокументировано
// HTTP_NO_CONTENT = 204
class NoContentException internal constructor(override val message: String) : ServerException(message)

// HTTP_GATEWAY_TIMEOUT = 504
class ServiceTimeoutException internal constructor(override val message: String) : ServerException(message)

// else
class UnknownServerException internal constructor(override val message: String) : ServerException(message)
/// не задокументировано