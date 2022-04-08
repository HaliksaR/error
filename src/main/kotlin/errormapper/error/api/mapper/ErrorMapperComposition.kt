package errormapper.error.api.mapper

import errormapper.error.api.exception.DomainException
import errormapper.error.api.exception.UnknownException
import errormapper.error.api.internal.Mapper

open class ErrorMapperComposition internal constructor(
    internal val mappersByCode: Map<Int, Mapper<DomainException>>,
    internal val otherCodesMapper: Mapper<DomainException>
) {

    internal companion object EMPTY : ErrorMapperComposition(emptyMap(), ::UnknownException)

    // не уверен что этот оператор подходит к текущей операции...
    operator fun plus(
        other: ErrorMapperComposition
    ): ErrorMapperComposition =
        ErrorMapperComposition(
            // вопрос, чьи значения при коллизии приоритетнее, по сути нужно старое перетирать новым
            mappersByCode = this.mappersByCode + other.mappersByCode,
            // вопрос, чей обработчик в таком случае приоритетнее
            otherCodesMapper = getOtherCodesMapper(
                old = this.otherCodesMapper,
                other = other.otherCodesMapper
            )
        )

    // не перетираем старые значения, если новый не переопределен.
    private fun getOtherCodesMapper(
        old: Mapper<DomainException>,
        other: Mapper<DomainException>
    ): Mapper<DomainException> =
        if (other == EMPTY.otherCodesMapper) {
            old
        } else {
            other
        }

    operator fun invoke(code: Int, message: String): DomainException =
        mappersByCode[code]?.invoke(message) ?: otherCodesMapper(message)
}




