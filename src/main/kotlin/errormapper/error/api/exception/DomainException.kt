package errormapper.error.api.exception

import errormapper.error.api.deprecated.ErrorCode
import errormapper.error.api.deprecated.HasErrorCode
import java.io.IOException

// маркер, чисто для when. DomainException не sealed, нужно наследование вне пакета.
sealed interface CommonDomainException

// доменные ошибки = коды из тела ответов
abstract class DomainException(
    override val message: String,
    override val errorCode: ErrorCode,
) : IOException(message), HasErrorCode

/// общие ошибочки
// HTTP_UNAVAILABLE = 503, есть код в ответе = 2
class ServiceUnavailableException(override val message: String) : DomainException(message, ErrorCode.ServiceUnavailable), CommonDomainException

// HTTP_BAD_REQUEST = 400, есть код в ответе = 3
class BadParamException(override val message: String) : DomainException(message, ErrorCode.BadParam), CommonDomainException

// HTTP_NOT_FOUND = 404, есть код в ответе = 4
class NotFoundException(override val message: String) : DomainException(message, ErrorCode.NotFound), CommonDomainException

// HTTP_UNAUTHORIZED = 401, есть код в ответе = 7
class AccessDeniedException(override val message: String) : DomainException(message, ErrorCode.AccessDenied), CommonDomainException

// HTTP_UNAUTHORIZED = 401, есть код в ответе = 8
class SessionExpiredException(override val message: String) : DomainException(message, ErrorCode.SessionExpired), CommonDomainException

// HTTP_BAD_REQUEST = 400, есть код в ответе = 400
class OutdatedVersionException(override val message: String) : DomainException(message, ErrorCode.OutdatedVersion), CommonDomainException

// HTTP_BAD_REQUEST = 500, есть код в ответе = 1
class InnerException(override val message: String) : DomainException(message, ErrorCode.Inner), CommonDomainException

// незадокументировано, нужен для обработок в маперах где не указали обработку коду.
class UnknownException(override val message: String) : DomainException(message, ErrorCode.Unknown), CommonDomainException
/// общие ошибочки