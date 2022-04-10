package errormapper.error.api.mapper

import errormapper.error.api.exception.DomainException
import errormapper.error.api.internal.Mapper

fun ErrorMapperComposition(
    baseComposition: ErrorMapperComposition = ErrorMapperComposition,
    builder: ErrorMapperCompositionBuilder.() -> Unit = {}
): ErrorMapperComposition =
    ErrorMapperCompositionBuilderImpl(baseComposition)
        .apply(builder)
        .build()


interface ErrorMapperCompositionBuilder {
    operator fun <R : DomainException> Int.invoke(map:  Mapper<R>)

    operator fun <R : DomainException> IntRange.invoke(map: Mapper<R>)

    fun <R : DomainException> other(map: Mapper<R>)
}

private class ErrorMapperCompositionBuilderImpl(
    baseComposition: ErrorMapperComposition
) : ErrorMapperCompositionBuilder {

    private val mappersByCode: MutableMap<Int, Mapper<DomainException>> =
        baseComposition.mappersByCode.toMutableMap()

    private var otherCodesMapper: Mapper<DomainException> =
        baseComposition.otherCodesMapper

    override fun <R : DomainException> Int.invoke(map: Mapper<R>) {
        mappersByCode[this] = map
    }

    override fun <R : DomainException> IntRange.invoke(map: Mapper<R>) {
        forEach { mappersByCode[it] = map }
    }

    override fun <R : DomainException> other(map: Mapper<R>) {
        otherCodesMapper = map
    }

    fun build(): ErrorMapperComposition =
        ErrorMapperComposition(mappersByCode, otherCodesMapper)
}