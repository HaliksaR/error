package handler

import errormapper.error.api.exception.*
import kotlinx.coroutines.*
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf

// эксперименты
fun main() = runBlocking {

    val int: Int = Try<Int> {
        withTimeout(0) {
            delay(444)
            44
        }
    }.catchTimeout {
        println("catchTimeout")
        4355
    }.catchDomain(marker = CommonDomainException::class) {
        handleDomainException(it)
        45
    }.catchServer {
        handleServerException(it)
        7890
    }.catchLocalNetwork {
        handleLocalNetworkException(it)
        89780
    }.catch(IllegalArgumentException::class) {
        println("catch IllegalArgumentException")
        546
    }.catch {
        println("catch")
        7689
    }.finally {
        println("finally")
        33
    }

    val int2: Unit = Try(ignoreCancellation = true) { throw CancellationException("F") }
        .catchTimeout { println("catchTimeout") }
        .catchDomain(marker = CommonDomainException::class, handle = ::handleDomainException)
        .catchServer(::handleServerException)
        .catchLocalNetwork(::handleLocalNetworkException)
        .catch(kClass = IllegalArgumentException::class) { println("catch IllegalArgumentException") }
        .finallyCatch { println("finallyCatch") }

    val int3: Unit = Try(ignoreCancellation = true) { throw IllegalStateException("F") }
        .finally {  }
}


private fun handleDomainException(exception: CommonDomainException) {
    println("catchDomain CommonDomainException")
    when (exception) {
        is AccessDeniedException -> {}
        is BadParamException -> {}
        is InnerException -> {}
        is NotFoundException -> {}
        is OutdatedVersionException -> {}
        is ServiceUnavailableException -> {}
        is SessionExpiredException -> {}
        is UnknownException -> {}
    }
}

private fun handleServerException(exception: ServerException) {
    println("catchServer")
    when (exception) {
        is NoContentException -> {}
        is ServerInnerException -> {}
        is ServiceTimeoutException -> {}
        is UnknownServerException -> {}
        is UnsupportedApiException -> {}
        is UnsupportedMethodException -> {}
    }
}

private fun handleLocalNetworkException(exception: LocalNetworkException) {
    println("catchLocalNetwork")
    when (exception) {
        is NetworkConnectException -> {}
        is NetworkTimeoutException -> {}
        is NetworkUnknownException -> {}
    }
}

fun <R> Try(block: suspend () -> R): Catching<R> =
    CatchingType<R>(block, ignoreCancellation = false)


fun Try(ignoreCancellation: Boolean, block: () -> Unit): Catching<Unit> =
    CatchingType<Unit>(block, ignoreCancellation)

interface Catching<R> {

    suspend fun <Marker : Any> catchDomain(marker: KClass<Marker>, handle: (exception: Marker) -> R): Catching<R>

    suspend fun catchLocalNetwork(handle: (exception: LocalNetworkException) -> R): Catching<R>

    suspend fun catchServer(handle: (exception: ServerException) -> R): Catching<R>

    suspend fun catchTimeout(handle: (exception: TimeoutCancellationException) -> R): Catching<R>

    suspend fun catch(kClass: KClass<out Exception>, handle: (exception: Exception) -> R): Catching<R>

    suspend fun catch(handle: (exception: Exception) -> R): Catching<R>

    suspend fun finallyCatch(handle: (exception: Exception) -> R): R

    suspend fun finally(handle: () -> R): R
}

@Suppress("UNCHECKED_CAST")
private class CatchingType<R>(
    private val block: suspend () -> R,
    private val ignoreCancellation: Boolean,
) : Catching<R> {

    private val handlers = mutableMapOf<KClass<out Any>, (exception: Any) -> R>()

    override suspend fun <Marker : Any> catchDomain(
        marker: KClass<Marker>,
        handle: (exception: Marker) -> R
    ): Catching<R> {
        handlers[marker] = handle as (Any) -> R
        return this
    }

    override suspend fun catchLocalNetwork(handle: (exception: LocalNetworkException) -> R): Catching<R> {
        handlers[LocalNetworkException::class] = handle as (Any) -> R
        return this
    }

    override suspend fun catchServer(handle: (exception: ServerException) -> R): Catching<R> {
        handlers[ServerException::class] = handle as (Any) -> R
        return this
    }

    override suspend fun catchTimeout(handle: (exception: TimeoutCancellationException) -> R): Catching<R> {
        handlers[TimeoutCancellationException::class] = handle as (Any) -> R
        return this
    }

    override suspend fun catch(kClass: KClass<out Exception>, handle: (exception: Exception) -> R): Catching<R> {
        handlers[kClass::class] = handle as (Any) -> R
        return this
    }

    override suspend fun catch(handle: (exception: Exception) -> R): Catching<R> {
        handlers[Exception::class] = handle as (Any) -> R
        return this
    }

    override suspend fun finallyCatch(handle: (exception: Exception) -> R): R {
        handlers[Exception::class] = handle as (Any) -> R
        return baseCatching(
            block = block,
            ignoreCancellationException = ignoreCancellation,
            handlers = handlers,
            finally = null
        ) as R
    }

    override suspend fun finally(handle: () -> R): R =
        baseCatching(
            block = block,
            ignoreCancellationException = ignoreCancellation,
            handlers = handlers,
            finally = handle
        ) as R
}

private suspend fun <R> baseCatching(
    block: suspend () -> R,
    ignoreCancellationException: Boolean,
    handlers: Map<KClass<out Any>, (Any) -> R>,
    finally: (() -> R)?,
): R? =
    try {
        block()
    } catch (exception: TimeoutCancellationException) {
        val handler = handlers
            .filter { it.key.isSubclassOf(exception::class) }
            .map { it.value }
            .firstOrNull()
        handler?.invoke(exception) ?: throw exception
    } catch (exception: CancellationException) {
        if (ignoreCancellationException) null else throw exception
    } catch (exception: Exception) {
        val handler = handlers
            .filter { exception::class.isSubclassOf(it.key) }
            .map { it.value }
            .firstOrNull()
        handler?.invoke(exception) ?: throw exception
    } finally {
        finally?.invoke()
    }