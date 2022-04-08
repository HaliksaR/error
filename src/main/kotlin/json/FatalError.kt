package json

import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.exceptions.UndeliverableException
import io.reactivex.rxjava3.functions.Consumer
import io.reactivex.rxjava3.kotlin.blockingSubscribeBy
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() {
    rx()
    coroutine()
}

private fun rx() {
    RxJavaPlugins.setErrorHandler(RxErrorHandler())

    // io/reactivex/rxjava3/internal/operators/single/SingleCreate.java:40
    Single.create<Int> { throw OutOfMemoryError("Single") }
        .blockingSubscribeBy({ println(it.message) })
}

private fun coroutine() = runBlocking {
    // kotlin/coroutines/jvm/internal/ContinuationImpl.kt:37
    launch(SupervisorJob() + CoroutineExceptionHandler { _, throwable ->
        if (throwable !is Error) println(throwable.message) else throw throwable
    }) {
        throw OutOfMemoryError("launch")
    }
}

class RxErrorHandler : Consumer<Throwable> {

    override fun accept(exception: Throwable) {
        when (exception) {
            is UndeliverableException -> println(exception.cause ?: exception)

            is NullPointerException,
            is IllegalStateException,
            is IllegalArgumentException ->
                Thread.currentThread()
                    .uncaughtExceptionHandler
                    .uncaughtException(Thread.currentThread(), exception)

            else -> println(exception)
        }
    }
}