package errormapper.error.api.exception

import java.io.IOException

// локальные проблемы
sealed class LocalNetworkException(override val message: String) : IOException(message)

/// не задокументировано
// локальные ошибки UnknownHostException, ConnectException, NoRouteToHostException
class NetworkConnectException internal constructor(override val message: String) : LocalNetworkException(message)

// локальные ошибки SocketTimeoutException
class NetworkTimeoutException internal constructor(override val message: String) : LocalNetworkException(message)

// локальные ошибки else
class NetworkUnknownException internal constructor(override val message: String) : LocalNetworkException(message)
/// не задокументировано