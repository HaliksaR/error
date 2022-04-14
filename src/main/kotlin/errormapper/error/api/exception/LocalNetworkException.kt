package errormapper.error.api.exception

import errormapper.error.api.deprecated.ErrorCode
import errormapper.error.api.deprecated.HasErrorCode
import java.io.IOException

// локальные проблемы
sealed class LocalNetworkException(
    override val message: String,
    override val errorCode: ErrorCode,
) : IOException(message), HasErrorCode

/// не задокументировано
// локальные ошибки UnknownHostException, ConnectException, NoRouteToHostException
class NetworkConnectException(override val message: String) : LocalNetworkException(message, ErrorCode.NetworkConnect)

// локальные ошибки SocketTimeoutException
class NetworkTimeoutException(override val message: String) : LocalNetworkException(message, ErrorCode.Timeout)

// локальные ошибки else
class NetworkUnknownException(override val message: String) : LocalNetworkException(message, ErrorCode.Unknown)
/// не задокументировано