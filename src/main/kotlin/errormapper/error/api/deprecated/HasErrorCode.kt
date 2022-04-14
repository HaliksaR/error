package errormapper.error.api.deprecated

@Deprecated("ErrorCode is deprecated")
interface HasErrorCode {
    val errorCode: ErrorCode
}