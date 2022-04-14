package errormapper.error.api.deprecated

@Deprecated("ErrorCode is deprecated")
class ErrorConverter {

    operator fun invoke(throwable: Throwable): ErrorCode =
        when (throwable) {
            is HasErrorCode -> throwable.errorCode
            else -> ErrorCode.Unknown
        }
}