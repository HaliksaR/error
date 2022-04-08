package errormapper

import errormapper.error.api.exception.*
import java.io.IOException


private fun playGroundWhenLocalNetworkException(exception: IOException) {
    when (exception as LocalNetworkException) {
        is NetworkConnectException -> TODO()
        is NetworkTimeoutException -> TODO()
        is NetworkUnknownException -> TODO()
    }
}

private fun playGroundWhenServerException(exception: IOException) {
    when (exception as ServerException) {
        is NoContentException -> TODO()
        is ServerInnerException -> TODO()
        is ServiceTimeoutException -> TODO()
        is UnknownServerException -> TODO()
        is UnsupportedApiException -> TODO()
        is UnsupportedMethodException -> TODO()
    }
}

private fun playGroundWhenDomainException(exception: IOException) {
    when (exception as DomainException) {
        // используем маркеры
    }
}

// вопрос удобного перекрытия CommonException и приколов с автоподстановкой when
private fun playGroundWhenCommon(exception: IOException) {
    when (exception as CommonDomainException) {
        is AccessDeniedException -> TODO()
        is BadParamException -> TODO()
        is InnerException -> TODO()
        is NotFoundException -> TODO()
        is OutdatedVersionException -> TODO()
        is ServiceUnavailableException -> TODO()
        is SessionExpiredException -> TODO()
        is UnknownException -> TODO()
    }
}

sealed interface PlayGroundMarker
sealed interface PlayGroundMarker2

class PlayGroundDomainException0(override val message: String) : DomainException(message), PlayGroundMarker2
class PlayGroundDomainException1(override val message: String) : DomainException(message), PlayGroundMarker
class PlayGroundDomainException2(override val message: String) : DomainException(message), PlayGroundMarker2
class PlayGroundDomainException3(override val message: String) : DomainException(message), PlayGroundMarker

private fun playGroundWhenPlayGroundMarker(exception: IOException) {
    when (exception as PlayGroundMarker) {
        is PlayGroundDomainException1 -> TODO()
        is PlayGroundDomainException3 -> TODO()
    }
}

private fun playGroundWhenMany(exception: IOException) {
    handleDomainException(exception as DomainException)
}

private fun handleDomainException(exception: DomainException) {
    when (exception) {
        is PlayGroundMarker -> handlePlayGroundMarker(exception)
        is PlayGroundMarker2 -> handlePlayGroundMarker2(exception)
        else -> {}
    }
}

fun handlePlayGroundMarker(exception: PlayGroundMarker) {
    when (exception) {
        is PlayGroundDomainException1 -> TODO()
        is PlayGroundDomainException3 -> TODO()
    }
}

fun handlePlayGroundMarker2(exception: PlayGroundMarker2) {
    when (exception) {
        is PlayGroundDomainException0 -> TODO()
        is PlayGroundDomainException2 -> TODO()
    }
}