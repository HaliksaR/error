package errormapper.feature.data.loan

import errormapper.domain.LoanDomain
import errormapper.error.api.ErrorMapper
import retrofit2.http.GET

@kotlinx.serialization.Serializable
data class Answer(val value: Int)

interface Api {

    @GET("loan/answer400")
    @ErrorMapper(LoanErrorMapper::class)
    suspend fun get400(): Answer

    @GET("loan/answer503")
    @ErrorMapper(LoanErrorMapper::class)
    suspend fun get503(): Answer

    @GET("loan/answer200")
    @ErrorMapper(LoanErrorMapper::class)
    suspend fun get200(): Answer
}


// аннотации-метки, чтобы на стороне vm можно было сразу понимать, какие ошибки нужно будет обработать
@LoanDomain
class LoanUseCase(private val api: Api) {

    suspend fun get400(): Answer =
        api.get400()

    suspend fun get503(): Answer =
        api.get400()

    suspend fun get200(): Answer =
        api.get400()
}