package errormapper.error.api

import errormapper.error.api.mapper.ErrorMapper
import kotlin.reflect.KClass

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class ErrorMapper(val mapper: KClass<out ErrorMapper>)