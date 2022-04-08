package errormapper.feature.data.loan

import errormapper.error.api.dsl.ErrorMapperComposition
import errormapper.error.api.mapper.ErrorMapper
import errormapper.error.api.mapper.CommonErrorMapper
import errormapper.error.api.exception.DomainException

class LoanErrorMapper : ErrorMapper {

    companion object {

        val composition = ErrorMapperComposition(CommonErrorMapper.composition) {
            1(::LoanException)
            2 { message -> LoanException(message) }
            5 { message -> LoanException(message) }
            8 { message -> LoanException(message) }
            9 { message -> LoanException(message) }
            0 { message -> LoanException(message) }
            (34..55) { message -> LoanException(message) }
        }
    }

    override fun invoke(code: Int, message: String): DomainException =
        composition(code, message)
}
