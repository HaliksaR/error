package errormapper.feature.data.crossdomain

import errormapper.error.api.mapper.ErrorMapperComposition
import errormapper.error.api.mapper.ErrorMapper
import errormapper.error.api.exception.DomainException
import errormapper.feature.data.loan.LoanErrorMapper
import errormapper.feature.data.transfer.TransferErrorMapper

object CrossDomainErrorMapper : ErrorMapper {

    // проблемы в том, что на экране могут обработать не правильно.
    val composition = ErrorMapperComposition(LoanErrorMapper.composition + TransferErrorMapper.composition)

    override fun invoke(code: Int, message: String): DomainException =
        composition.invoke(code, message)
}