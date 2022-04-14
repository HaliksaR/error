package errormapper.feature.data.transfer

import errormapper.error.api.deprecated.ErrorCode
import errormapper.error.api.exception.DomainException

sealed interface TransferException

class TransferNotFoundException(override val message: String) : DomainException(message, ErrorCode.NotFound), TransferException

class TransferUnknownException(override val message: String) : DomainException(message, ErrorCode.Unknown), TransferException

class TransferCountryException(override val message: String) : DomainException(message, ErrorCode.Unknown), TransferException