package errormapper

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import errormapper.error.api.interceptor.ErrorInterceptor
import errormapper.error.api.internal.ErrorModel
import errormapper.feature.data.crossdomain.ProfileScenario
import errormapper.feature.data.loan.*
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

private val mockRequests: (String) -> Pair<Int, String> = { encodedPath: String ->
    when {
        encodedPath.contains("/answer200") -> 200 to "{\"value\":1}"
        encodedPath.contains("/answer400") -> 400 to jsonize(ErrorModel(code = 1, message = "answer400"))
        encodedPath.contains("/answer503") -> 503 to jsonize(ErrorModel(code = 2, message = "answer503"))
        else -> 404 to "{}"
    }
}

fun main(): Unit = runBlocking {
    val scope = CoroutineScope(SupervisorJob() + Dispatchers.Unconfined)
    val loanUseCase = LoanUseCase(loanApi)
    val transferUseCase = TransferUseCase(transferApi)
    scope.launch(
        body = { println("success : ${loanUseCase.get400()}") },
        error = { exception ->
            if (exception is LoanException) {
                when (exception) {
                    is LoanCountryException,
                    is LoanNotFoundException,
                    is LoanUnknownException -> println("LoanException : $exception")
                }
            } else {
                println("Unknown error : $exception")
            }
        }
    )
    scope.launch(
        body = { println("success : ${transferUseCase.get400()}") },
        error = { exception ->
            if (exception is TransferException) {
                when (exception) {
                    is TransferCountryException,
                    is TransferNotFoundException,
                    is TransferUnknownException -> println("TransferException : $exception")
                }
            } else {
                println("Unknown error : $exception")
            }
        }
    )

    val profileScenario = ProfileScenario(loanUseCase, transferUseCase)
    scope.launch(
        body = { println("success : ${profileScenario.get400()}") },
        error = { exception ->
            // благодаря аннотациям над сценарием, понятно ошибки какого домена могут прилететь.
            when (exception) {
                is TransferException -> println("TransferException : $exception")
                is LoanException -> println("TransferException : $exception")
                else -> println("Unknown error : $exception")
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

private val loanApi: errormapper.feature.data.loan.Api =
    Retrofit.Builder().baseUrl("https://test")
        .addConverterFactory(Json.asConverterFactory(MEDIA_TYPE_JSON.toMediaType()))
        .client(client)
        .build()
        .create()

private val transferApi: errormapper.feature.data.transfer.Api =
    Retrofit.Builder().baseUrl("https://test")
        .addConverterFactory(Json.asConverterFactory(MEDIA_TYPE_JSON.toMediaType()))
        .client(client)
        .build()
        .create()


