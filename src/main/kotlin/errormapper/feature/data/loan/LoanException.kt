package errormapper.feature.data.loan

import errormapper.error.api.exception.DomainException

sealed interface LoanException

class LoanNotFoundException(override val message: String) : DomainException(message), LoanException

class LoanUnknownException(override val message: String) : DomainException(message), LoanException

class LoanCountryException(override val message: String) : DomainException(message), LoanException