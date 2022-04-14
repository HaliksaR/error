package errormapper.error.api.deprecated

@Deprecated("ErrorCode is deprecated")
enum class ErrorCode(val code: Int) {
    Unknown(0),
    Inner(1),
    ServiceUnavailable(2),
    BadParam(3),
    NotFound(4),
    AccessDenied(7),
    SessionExpired(8),
    OutdatedVersion(400),
    NetworkConnect(-1),
    Timeout(-2),
    NoContent(-3),
    UnsupportedMethod(-4),
    UnsupportedApi(-5),
}