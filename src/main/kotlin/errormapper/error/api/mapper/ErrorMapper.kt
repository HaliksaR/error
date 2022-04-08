package errormapper.error.api.mapper

import errormapper.error.api.exception.DomainException

fun interface ErrorMapper {

    operator fun invoke(code: Int, message: String): DomainException
}