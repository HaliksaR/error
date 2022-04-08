package errormapper.feature.data.loan

import errormapper.error.api.exception.DomainException

class LoanException(override val message: String) : DomainException(message)