package errormapper.error.api.mapper

import errormapper.error.api.exception.*


object CommonErrorMapper : ErrorMapper {

    // можно даже внедрить как дефотное значение в сам ErrorMapperComposition
    // но не нравки что он уже будет загружен сразу какой-то инфой :(
    val composition = ErrorMapperComposition {
        1 { message -> InnerException(message) }
        2 { message -> ServiceUnavailableException(message) }
        3 { message -> BadParamException(message) }
        4 { message -> NotFoundException(message) }
        7 { message -> AccessDeniedException(message) }
        8 { message -> SessionExpiredException(message) }
    }

    override fun invoke(code: Int, message: String): DomainException =
        composition(code, message)
}