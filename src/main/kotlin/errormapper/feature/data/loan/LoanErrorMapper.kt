package errormapper.feature.data.loan

import errormapper.error.api.exception.DomainException
import errormapper.error.api.exception.UnknownException
import errormapper.error.api.mapper.CommonErrorMapper
import errormapper.error.api.mapper.ErrorMapper
import errormapper.error.api.mapper.ErrorMapperComposition

class LoanErrorMapper : ErrorMapper {

    companion object {

        val composition = ErrorMapperComposition(CommonErrorMapper.composition) {
            12 { message -> LoanNotFoundException(message) }
            1 { message -> LoanUnknownException(message) }
            6 { message -> LoanCountryException(message) }
            (34..55) { message -> UnknownException(message) }
        }
    }

    override fun invoke(code: Int, message: String): DomainException =
        composition(code, message)
}
