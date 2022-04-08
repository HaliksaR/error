package errormapper.error.api.interceptor

import errormapper.error.api.internal.createDomainException
import errormapper.error.api.internal.createLocalNetworkException
import errormapper.error.api.internal.createServerException
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.Response
import sun.net.www.protocol.http.HttpURLConnection.HTTP_OK
import java.io.IOException

class ErrorInterceptor(private val serializer: Json, private val context: String) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = try {
            chain.proceed(request)
        } catch (proceedException: IOException) {
            throw createLocalNetworkException(proceedException, context)
        }
        return if (response.code == HTTP_OK) {
            response
        } else {
            throw createDomainException(request, response, serializer, context)
                ?: createServerException(response)
        }
    }
}


