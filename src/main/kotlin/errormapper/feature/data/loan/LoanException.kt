package errormapper.feature.data.loan

import errormapper.error.api.deprecated.ErrorCode
import errormapper.error.api.exception.DomainException

sealed interface LoanException

class LoanNotFoundException(override val message: String) : DomainException(message, ErrorCode.NotFound), LoanException

class LoanUnknownException(override val message: String) : DomainException(message, ErrorCode.Unknown), LoanException

class LoanCountryException(override val message: String) : DomainException(message, ErrorCode.Unknown), LoanException