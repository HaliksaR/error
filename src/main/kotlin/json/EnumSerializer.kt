package json

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.util.*
import java.util.regex.Pattern

open class EnumStringSerializer<T : Any>(
    private val values: Array<T>
) : KSerializer<T> {

    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("EnumString", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): T {
        val enumValue = decoder.decodeString()
        return values.firstOrNull { it.toLowercase() == enumValue.toLowercase() }
            ?: throw IllegalStateException("No enum value $enumValue")
    }

    override fun serialize(encoder: Encoder, value: T) {
        encoder.encodeString(value.toCamelCaseString())
    }

    private fun Any.toLowercase(): String =
        toString()
            .replace("_", "")
            .replace(".", "")
            .lowercase(Locale.ENGLISH)

    private fun Any.toCamelCaseString(): String {
        val pattern = Pattern.compile("[a-z0-9]+")
        val string = this.toString().lowercase(Locale.ENGLISH)
        val matcher = pattern.matcher(string)

        var result = ""
        var word: String

        while (matcher.find()) {
            word = matcher.group()

            result += when {
                result.isEmpty() -> word
                else -> word.substring(0, 1).uppercase(Locale.ENGLISH) + word.substring(1).lowercase(Locale.ENGLISH)
            }
        }

        return result
    }
}

@Serializable(TestClass.Serializer::class)
enum class TestClass {

    SERIAL_SERIAL;

    object Serializer : EnumStringSerializer<TestClass>(values())
}
