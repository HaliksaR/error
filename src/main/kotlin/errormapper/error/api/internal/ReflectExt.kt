package errormapper.error.api.internal

import errormapper.error.api.mapper.ErrorMapper
import okhttp3.Request
import retrofit2.Invocation
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.primaryConstructor


// по хорошему крашить приложение, так как на бой уйдет запрос без мапера... java.lang.Error.
// снова попался на java.io.IOException: canceled due to ErrorMapperNotFoundError
// Exception in thread "OkHttp Dispatcher" Exception in thread "OkHttp Dispatcher"
// Exception in thread "OkHttp Dispatcher" ErrorMapperNotFoundError
internal fun Request.errorMapper(context: String): ErrorMapper =
    tag(Invocation::class.java)
        ?.method()
        ?.getAnnotation(errormapper.error.api.ErrorMapper::class.java)
        ?.mapper
        ?.create(context)
        ?: throw ErrorMapperNotFoundError("ErrorMapper for $url not found")

private fun <T : ErrorMapper> KClass<T>.create(context: String): T? =
    objectInstance ?: primaryConstructor?.safeCall(context)

private fun <T : ErrorMapper> KFunction<T>.safeCall(context: String): T? =
    when (parameters.size) {
        0 -> call()
        1 -> call(context)
        else -> null
    }