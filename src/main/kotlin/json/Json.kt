@file:OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class, ExperimentalStdlibApi::class)

package json

import kotlinx.serialization.*
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import kotlin.reflect.KClass
import kotlin.reflect.typeOf

@Serializable
enum class Test {
    TEST
}

@Serializable
data class TestData<T>(val value: T)

class DynamicLookupSerializer : KSerializer<Any> {

    override val descriptor: SerialDescriptor =
        ContextualSerializer(Any::class, null, emptyArray()).descriptor

    override fun serialize(encoder: Encoder, value: Any) {
        val actualSerializer = encoder.serializersModule.getContextual(value::class)
            ?: value::class.serializer()
        encoder.encodeSerializableValue(actualSerializer as KSerializer<Any>, value)
    }

    override fun deserialize(decoder: Decoder): Any {
        error("Unsupported")
    }
}

val serializer = Json {
    serializersModule = SerializersModule {
        if (enabledDynamicLookupSerializer) {
            contextual(DynamicLookupSerializer())
        }
    }
}


const val simplePrint = false
const val enabledDynamicLookupSerializer = false

fun main() {
    val map: Map<String, Boolean> = mapOf("one" to false, "two" to true)

    println("---------Map<String, Boolean>----------")
    toJsonReified(map)
    toJsonTypeOf(map)
    toJsonTypeReferenceReified(map)
    toJsonClass(map)
    toJsonClassJava(map)
    toJsonSerializerClass(map, Map::class.java)
    toJsonSerializerClassReified(map, Map::class.java)
    toJsonSerializerKClass(map, Map::class)
    toJsonSerializerKClassReified(map, Map::class)
    toJsonTypeReference(map)

    val mapList: Map<String, List<Boolean>> = mapOf("one" to listOf(false), "two" to listOf(true))

    println("---------Map<String, List<Boolean>>----------")
    toJsonReified(mapList)
    toJsonTypeOf(mapList)
    toJsonTypeReferenceReified(mapList)
    toJsonClass(mapList)
    toJsonClassJava(mapList)
    toJsonSerializerClass(mapList, Map::class.java)
    toJsonSerializerClassReified(mapList, Map::class.java)
    toJsonSerializerKClass(mapList, Map::class)
    toJsonSerializerKClassReified(mapList, Map::class)
    toJsonTypeReference(mapList)

    val mapAny: Map<Any, Any> = mapOf(listOf<Any>("ff") to 1, "two" to 2L)

    println("---------Map<Any, Any>----------")
    toJsonReified(mapAny)
    toJsonTypeOf(mapAny)
    toJsonTypeReferenceReified(mapAny)
    toJsonClass(mapAny)
    toJsonClassJava(mapAny)
    toJsonSerializerClass(mapAny, Map::class.java)
    toJsonSerializerClassReified(mapAny, Map::class.java)
    toJsonSerializerKClass(mapAny, Map::class)
    toJsonSerializerKClassReified(mapAny, Map::class)
    toJsonTypeReference(mapAny)

    val list: List<String> = listOf("one", "two")

    println("---------List<String>----------")
    toJsonReified(list)
    toJsonTypeOf(list)
    toJsonTypeReferenceReified(list)
    toJsonClass(list)
    toJsonClassJava(list)
    toJsonSerializerClass(list, List::class.java)
    toJsonSerializerClassReified(list, List::class.java)
    toJsonSerializerKClass(list, List::class)
    toJsonSerializerKClassReified(list, List::class)
    toJsonTypeReference(list)

    val listAny: List<Any> = listOf("one", false)

    println("---------List<Any>----------")
    toJsonReified(listAny)
    toJsonTypeOf(listAny)
    toJsonTypeReferenceReified(listAny)
    toJsonClass(listAny)
    toJsonClassJava(listAny)
    toJsonSerializerClass(listAny, List::class.java)
    toJsonSerializerClassReified(listAny, List::class.java)
    toJsonSerializerKClass(listAny, List::class)
    toJsonSerializerKClassReified(listAny, List::class)
    toJsonTypeReference(listAny)

    val any: Any = listOf("one", false)

    println("---------Any----------")
    toJsonReified(any)
    toJsonTypeOf(any)
    toJsonTypeReferenceReified(any)
    toJsonClass(any)
    toJsonClassJava(any)
    toJsonSerializerClass(any, Any::class.java)
    toJsonSerializerClassReified(any, Any::class.java)
    toJsonSerializerKClass(any, Any::class)
    toJsonSerializerKClassReified(any, Any::class)
    toJsonTypeReference(any)

    val enum: Test = Test.TEST

    println("---------Test----------")
    toJsonReified(enum)
    toJsonTypeOf(enum)
    toJsonTypeReferenceReified(enum)
    toJsonClass(enum)
    toJsonClassJava(enum)
    toJsonSerializerClass(enum, Test::class.java)
    toJsonSerializerClassReified(enum, Test::class.java)
    toJsonSerializerKClass(enum, Test::class)
    toJsonSerializerKClassReified(enum, Test::class)
    toJsonTypeReference(enum)

    val enumTest: Enum<Test> = Test.TEST

    println("---------Enum<Test>----------")
    toJsonReified(enumTest)
    toJsonTypeOf(enumTest)
    toJsonTypeReferenceReified(enumTest)
    toJsonClass(enumTest)
    toJsonClassJava(enumTest)
    toJsonSerializerClass(enumTest, Enum::class.java)
    toJsonSerializerClassReified(enumTest, Enum::class.java)
    toJsonSerializerKClass(enumTest, Enum::class)
    toJsonSerializerKClassReified(enumTest, Enum::class)
    toJsonTypeReference(enumTest)

    val enumAny: Enum<*> = Test.TEST

    println("---------Enum<*>----------")
    toJsonReified(enumAny)
    toJsonTypeOf(enumAny)
    toJsonTypeReferenceReified(enumAny)
    toJsonClass(enumAny)
    toJsonClassJava(enumAny)
    toJsonSerializerClass(enumAny, Enum::class.java)
    toJsonSerializerClassReified(enumAny, Enum::class.java)
    toJsonSerializerKClass(enumAny, Enum::class)
    toJsonSerializerKClassReified(enumAny, Enum::class)
    toJsonTypeReference(enumAny)

    val dataClass: TestData<Int> = TestData(1)

    println("--------TestData<Int>-----------")
    toJsonReified(dataClass)
    toJsonTypeOf(dataClass)
    toJsonTypeReferenceReified(dataClass)
    toJsonClass(dataClass)
    toJsonClassJava(dataClass)
    toJsonSerializerClass(dataClass, TestData::class.java)
    toJsonSerializerClassReified(dataClass, TestData::class.java)
    toJsonSerializerKClass(dataClass, TestData::class)
    toJsonSerializerKClassReified(dataClass, TestData::class)
    toJsonTypeReference(dataClass)

    val dataClassAny: TestData<Any> = TestData(1)

    println("--------TestData<Any>-----------")
    toJsonReified(dataClassAny)
    toJsonTypeOf(dataClassAny)
    toJsonTypeReferenceReified(dataClassAny)
    toJsonClass(dataClassAny)
    toJsonClassJava(dataClassAny)
    toJsonSerializerClass(dataClassAny, TestData::class.java)
    toJsonSerializerClassReified(dataClassAny, TestData::class.java)
    toJsonSerializerKClass(dataClassAny, TestData::class)
    toJsonSerializerKClassReified(dataClassAny, TestData::class)
    toJsonTypeReference(dataClassAny)

    val dataClassStar: TestData<*> = TestData(1)

    println("--------TestData<*>-----------")
    toJsonReified(dataClassStar)
    toJsonTypeOf(dataClassStar)
    toJsonClass(dataClassStar)
    toJsonClassJava(dataClassStar)
    toJsonSerializerClass(dataClassStar, TestData::class.java)
    toJsonSerializerClassReified(dataClassStar, TestData::class.java)
    toJsonSerializerKClass(dataClassStar, TestData::class)
    toJsonSerializerKClassReified(dataClassStar, TestData::class)
    toJsonTypeReference(dataClassStar)
    toJsonTypeReferenceReified(dataClassStar)

    val string: String = "string"

    println("--------String-----------")
    toJsonReified(string)
    toJsonTypeOf(string)
    toJsonClass(string)
    toJsonClassJava(string)
    toJsonSerializerClass(string, String::class.java)
    toJsonSerializerClassReified(string, String::class.java)
    toJsonSerializerKClass(string, String::class)
    toJsonSerializerKClassReified(string, String::class)
    toJsonTypeReference(string)
    toJsonTypeReferenceReified(string)

    val stringAny: Any = "stringAny"

    println("--------stringAny-----------")
    toJsonReified(stringAny)
    toJsonTypeOf(stringAny)
    toJsonClass(stringAny)
    toJsonClassJava(stringAny)
    toJsonSerializerClass(stringAny, Any::class.java)
    toJsonSerializerClassReified(stringAny, Any::class.java)
    toJsonSerializerKClass(stringAny, Any::class)
    toJsonSerializerKClassReified(stringAny, Any::class)
    toJsonTypeReference(stringAny)
    toJsonTypeReferenceReified(stringAny)

    val array: Array<Int> = arrayOf(1)

    println("--------Array<Int>-----------")
    toJsonReified(array)
    toJsonTypeOf(array)
    toJsonClass(array)
    toJsonClassJava(array)
    toJsonSerializerClass(array, Array<Int>::class.java)
    toJsonSerializerClassReified(array, Array<Int>::class.java)
    toJsonSerializerKClass(array, Array<Int>::class)
    toJsonSerializerKClassReified(array, Array<Int>::class)
    toJsonTypeReference(array)
    toJsonTypeReferenceReified(array)

    val arrayAny: Array<Any> = arrayOf(1)

    println("--------Array<Any>-----------")
    toJsonReified(arrayAny)
    toJsonTypeOf(arrayAny)
    toJsonClass(arrayAny)
    toJsonClassJava(arrayAny)
    toJsonSerializerClass(arrayAny, Array<Any>::class.java)
    toJsonSerializerClassReified(arrayAny, Array<Any>::class.java)
    toJsonSerializerKClass(arrayAny, Array<Any>::class)
    toJsonSerializerKClassReified(arrayAny, Array<Any>::class)
    toJsonTypeReference(arrayAny)
    toJsonTypeReferenceReified(arrayAny)


    val arrayStar: Array<*> = arrayOf(1)

    println("--------Array<*>-----------")
    toJsonReified(arrayStar)
    toJsonTypeOf(arrayStar)
    toJsonClass(arrayStar)
    toJsonClassJava(arrayStar)
    toJsonSerializerClass(arrayStar, Array::class.java)
    toJsonSerializerClassReified(arrayStar, Array::class.java)
    toJsonSerializerKClass(arrayStar, Array::class)
    toJsonSerializerKClassReified(arrayStar, Array::class)
    toJsonTypeReference(arrayStar)
    toJsonTypeReferenceReified(arrayStar)
}


inline fun <reified T> toJsonReified(value: T) =
    use("reified T") { serializer.encodeToString(value) }

fun <T : Any> toJsonClass(value: T) =
    use("value::class") {
        serializer.encodeToString(
            (serializer.serializersModule.getContextual(value::class) ?: value::class.serializer()) as KSerializer<T>,
            value
        )
    }

fun <T : Any> toJsonClassJava(value: T) =
    use("value::class.java") {
        serializer.encodeToString(
            serializer.serializersModule.serializer(value::class.java),
            value
        )
    }

inline fun <reified T : Any> toJsonTypeOf(value: T) =
    use("typeOf<T>()") { serializer.encodeToString(serializer.serializersModule.serializer(typeOf<T>()), value) }

fun <T : Any> toJsonSerializerClass(value: T, type: Class<T>) =
    use("Class<T>") { serializer.encodeToString(serializer.serializersModule.serializer(type), value) }

inline fun <reified T : Any> toJsonSerializerClassReified(value: T, type: Class<T>) =
    use("reified Class<T>") { serializer.encodeToString(serializer.serializersModule.serializer(type), value) }

fun <T : Any> toJsonSerializerKClass(value: T, type: KClass<T>) =
    use("KClass<T>") { serializer.encodeToString(type.serializer(), value) }

inline fun <reified T : Any> toJsonSerializerKClassReified(value: T, type: KClass<T>) =
    use("reified KClass<T>") { serializer.encodeToString(type.serializer(), value) }

fun <T : Any> toJsonTypeReference(value: T) =
    use("TypeReference<T>") {
        serializer.encodeToString(
            serializer = serializer.serializersModule.serializer(object : TypeReference<T>() {}.getType()),
            value = value
        )
    }

inline fun <reified T : Any> toJsonTypeReferenceReified(value: T) =
    use("reified TypeReference<T>") {
        serializer.encodeToString(
            serializer = serializer.serializersModule.serializer(object : TypeReference<T>() {}.getType()),
            value = value
        )
    }

inline fun use(type: String, block: () -> String): Unit =
    try {
        val result = block()
        println(type + " : " + if (simplePrint) "+" else result)
    } catch (throwable: Throwable) {
        println(type + " : " + if (simplePrint) "-" else throwable.message?.replace('\n', ' '))
    }


abstract class TypeReference<T>

fun <T> TypeReference<T>.getType(): Type =
    (javaClass.genericSuperclass as ParameterizedType)
        .actualTypeArguments[0]