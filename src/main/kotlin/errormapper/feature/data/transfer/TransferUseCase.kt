package errormapper.feature.data.transfer

import errormapper.domain.TransferDomain
import errormapper.error.api.ErrorMapper
import retrofit2.http.GET

@kotlinx.serialization.Serializable
data class Answer(val value: Int)

interface Api {

    @GET("transfer/answer400")
    @ErrorMapper(TransferErrorMapper::class)
    suspend fun get400(): Answer

    @GET("transfer/answer503")
    @ErrorMapper(TransferErrorMapper::class)
    suspend fun get503(): Answer

    @GET("transfer/answer200")
    @ErrorMapper(TransferErrorMapper::class)
    suspend fun get200(): Answer
}

// аннотации-метки, чтобы на стороне vm можно было сразу понимать, какие ошибки нужно будет обработать
@TransferDomain
class TransferUseCase(private val api: Api) {

    suspend fun get400(): Answer =
        api.get400()

    suspend fun get503(): Answer =
        api.get400()

    suspend fun get200(): Answer =
        api.get400()
}