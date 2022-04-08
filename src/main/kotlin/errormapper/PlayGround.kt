package errormapper

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import errormapper.error.api.ErrorMapper
import errormapper.error.api.exception.*
import errormapper.error.api.interceptor.ErrorInterceptor
import errormapper.error.api.internal.ErrorModel
import errormapper.error.api.mapper.CommonErrorMapper
import errormapper.feature.data.loan.LoanErrorMapper
import errormapper.feature.data.transfer.*
import kotlinx.coroutines.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Retrofit
import retrofit2.create
import retrofit2.http.GET
import java.net.NoRouteToHostException

@kotlinx.serialization.Serializable
data class Answer(val value: Int)

interface Api {

    @GET("/answer400")
    @ErrorMapper(LoanErrorMapper::class)
    suspend fun get400(): Answer

    @GET("/answerTransfer")
    @ErrorMapper(TransferErrorMapper::class)
    suspend fun getTransfer(): Answer

    @GET("/answerTransferUnknown")
    @ErrorMapper(TransferErrorMapper::class)
    suspend fun getTransferUnknown(): Answer

    @GET("/answerTransferNoRouteToHost")
    @ErrorMapper(CommonErrorMapper::class)
    suspend fun getTransferNoRouteToHost(): Answer

    @GET("/answerCommon")
    @ErrorMapper(CommonErrorMapper::class)
    suspend fun getCommon(): Answer

    @GET("/answer200")
    @ErrorMapper(CommonErrorMapper::class)
    suspend fun get200(): Answer

    @GET("/answer503")
    @ErrorMapper(CommonErrorMapper::class)
    suspend fun get503(): Answer
}

private val mockRequests: (String) -> Pair<Int, String> = { encodedPath: String ->
    when (encodedPath) {
        "/answer400" -> 400 to jsonize(ErrorModel(code = 1, message = "answer400"))
        "/answerTransfer" -> 400 to jsonize(ErrorModel(code = 6, message = "answerTransfer"))
        "/answerTransferUnknown" -> 400 to jsonize(ErrorModel(code = 4345, message = "answerTransferUnknown"))
        "/answerTransferNoRouteToHost" -> throw NoRouteToHostException("answerTransferNoRouteToHost")
        "/answerCommon" -> 400 to jsonize(ErrorModel(code = 2, message = "answerCommon"))
        "/answer200" -> 200 to jsonize(Answer(2))
        "/answer503" -> 503 to jsonize(ErrorModel(code = 2, message = "answer503"))
        else -> 404 to "{}"
    }
}

fun main(): Unit = runBlocking {
    val scope = CoroutineScope(SupervisorJob() + Dispatchers.Unconfined)
    scope.launch(
        body = { println("success : ${api.get200()}") },
        error = { println("error : $it") }
    )
    scope.launch(
        body = { println("success : ${api.get400()}") },
        error = { println("error : $it") }
    )
    scope.launch(
        body = { println("success : ${api.getCommon()}") },
        error = { println("error : $it") }
    )
    scope.launch(
        body = { println("success : ${api.getTransferNoRouteToHost()}") },
        error = { println("error : $it") }
    )
    scope.launch(
        body = { println("success : ${api.getTransfer()}") },
        error = { exception ->
            if (exception is TransferException) {
                when (exception) {
                    is TransferCountryException -> println("error : $exception")
                    is TransferNotFoundException -> println("error : $exception")
                    is TransferUnknownException -> println("error : $exception")
                }
            } else {
                println("Unknown error : $exception")
            }
        }
    )
    scope.launch(
        body = { println("success : ${api.getTransferUnknown()}") },
        error = { exception ->
            if (exception is TransferException) {
                when (exception) {
                    is TransferCountryException -> println("error : $exception")
                    is TransferNotFoundException -> println("error : $exception")
                    is TransferUnknownException -> println("error : $exception")
                }
            } else {
                println("Unknown error : $exception")
            }
        }
    )
    scope.launch(
        body = { println("success : ${api.get503()}") },
        error = { exception ->
            if (exception is TransferException) {
                when (exception) {
                    is TransferCountryException -> println("error : $exception")
                    is TransferNotFoundException -> println("error : $exception")
                    is TransferUnknownException -> println("error : $exception")
                }
            } else {
                println("Unknown error : $exception")
            }
        }
    )
}

private inline fun <reified T> jsonize(value: T): String =
    Json.encodeToString(value)

private fun <T> CoroutineScope.launch(body: suspend () -> T, error: (throwable: Throwable) -> Unit): Job =
    launch(CoroutineExceptionHandler { _, throwable -> error(throwable) }) { body() }

private const val CONTENT_TYPE = "content-type"
private const val MEDIA_TYPE_JSON = "application/json"

private fun mockInterceptor() = Interceptor { chain ->
    val request = chain.request()
    val (code, body) = mockRequests(chain.request().url.encodedPath)
    Response.Builder()
        .request(request)
        .protocol(Protocol.HTTP_2)
        .addHeader(CONTENT_TYPE, MEDIA_TYPE_JSON)
        .code(code)
        .message("mock")
        .body(body.toResponseBody(MEDIA_TYPE_JSON.toMediaType()))
        .build()
}

private val client: OkHttpClient =
    OkHttpClient.Builder()
        .addInterceptor(ErrorInterceptor(Json, "context"))
        .addInterceptor(mockInterceptor())
        .build()

private val api: Api =
    Retrofit.Builder().baseUrl("https://test")
        .addConverterFactory(Json.asConverterFactory(MEDIA_TYPE_JSON.toMediaType()))
        .client(client)
        .build()
        .create()


