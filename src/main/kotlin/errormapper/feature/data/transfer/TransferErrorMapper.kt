package errormapper.feature.data.transfer

import errormapper.error.api.dsl.ErrorMapperComposition
import errormapper.error.api.mapper.ErrorMapper
import errormapper.error.api.mapper.CommonErrorMapper
import errormapper.error.api.exception.DomainException
import errormapper.error.api.exception.UnknownException

class TransferErrorMapper(private val context: String) : ErrorMapper {

    companion object {

        val composition = ErrorMapperComposition(CommonErrorMapper.composition) {
            12 { message -> TransferNotFoundException(message) }
            1 { message -> TransferUnknownException(message) }
            6 { message -> TransferCountryException(message) }
            other { message -> UnknownException(message) }
        }
    }

    // неконсистентность при наследовании composition в других маперах т.к. теряется связка context + message
    override fun invoke(code: Int, message: String): DomainException =
        composition(code, context + message)
}