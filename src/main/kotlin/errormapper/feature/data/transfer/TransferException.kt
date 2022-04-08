package errormapper.feature.data.transfer

import errormapper.error.api.exception.DomainException

sealed interface TransferException

class TransferNotFoundException(override val message: String) : DomainException(message), TransferException

class TransferUnknownException(override val message: String) : DomainException(message), TransferException

class TransferCountryException(override val message: String) : DomainException(message), TransferException