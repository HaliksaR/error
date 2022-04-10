package errormapper.feature.data.crossdomain

import errormapper.domain.LoanDomain
import errormapper.domain.TransferDomain
import errormapper.feature.data.loan.LoanUseCase
import errormapper.feature.data.transfer.TransferUseCase

// аннотации-метки, чтобы на стороне vm можно было сразу понимать, какие ошибки нужно будет обработать
@LoanDomain
@TransferDomain
class ProfileScenario(
    private val loanUseCase: LoanUseCase,
    private val transferUseCase: TransferUseCase
) {

    suspend fun get400() {
        loanUseCase.get400()
        transferUseCase.get400()
    }


    suspend fun get503() {
        loanUseCase.get503()
        transferUseCase.get503()
    }


    suspend fun get200() {
        loanUseCase.get200()
        transferUseCase.get200()
    }

}