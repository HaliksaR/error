package errormapper.feature.data.deprecated

import errormapper.error.api.deprecated.ErrorCode
import errormapper.error.api.deprecated.ErrorConverter
import errormapper.error.api.exception.*
import errormapper.feature.data.loan.LoanCountryException
import errormapper.feature.data.loan.LoanException
import errormapper.feature.data.loan.LoanNotFoundException
import errormapper.feature.data.loan.LoanUnknownException

class SampleViewModel(
    private val errorConverter: ErrorConverter
) {

    fun loadData() {
        try {
            loadModel()
        } catch (exception: Throwable) {
            handleError(exception)
        }
    }

    private fun loadModel() {
        throw LoanNotFoundException("loadModel error")
    }

    private fun handleError(exception: Throwable) {
        val errorCode: ErrorCode = errorConverter(exception)
        when(errorCode) {
            ErrorCode.Unknown -> TODO()
            ErrorCode.Inner -> TODO()
            ErrorCode.ServiceUnavailable -> TODO()
            ErrorCode.BadParam -> TODO()
            ErrorCode.NotFound -> TODO()
            ErrorCode.AccessDenied -> TODO()
            ErrorCode.SessionExpired -> TODO()
            ErrorCode.OutdatedVersion -> TODO()
            ErrorCode.NetworkConnect -> TODO()
            ErrorCode.Timeout -> TODO()
            ErrorCode.NoContent -> TODO()
            ErrorCode.UnsupportedMethod -> TODO()
            ErrorCode.UnsupportedApi -> TODO()
        }
    }
}

class MigratedSampleViewModel {

    fun loadData() {
        try {
            loadModel()
        } catch (exception: Exception) {
            handleError(exception)
        }
    }

    private fun loadModel() {
        throw LoanNotFoundException("loadModel error")
    }

    private fun handleError(exception: Exception) {
        when(exception) {
            is LoanException -> handleDomainError(exception)
            is LocalNetworkException -> handleLocalNetworkError(exception)
            is ServerException -> handleServerError(exception)
            else -> println("Unknown Exception $exception")
        }
    }

    private fun handleDomainError(exception: LoanException) {
       when(exception) {
           is LoanCountryException -> TODO()
           is LoanNotFoundException -> TODO()
           is LoanUnknownException -> TODO()
       }
    }

    private fun handleLocalNetworkError(exception: LocalNetworkException) {
        when(exception) {
            is NetworkConnectException -> TODO()
            is NetworkTimeoutException -> TODO()
            is NetworkUnknownException -> TODO()
        }
    }

    private fun handleServerError(exception: ServerException) {
        when(exception) {
            is NoContentException -> TODO()
            is ServerInnerException -> TODO()
            is ServiceTimeoutException -> TODO()
            is UnknownServerException -> TODO()
            is UnsupportedApiException -> TODO()
            is UnsupportedMethodException -> TODO()
        }
    }
}