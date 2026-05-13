package com.main.alphatracer.model
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.UUID

object LocalDateSerializer : KSerializer<LocalDate> {
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("LocalDate", PrimitiveKind.STRING)
    override fun serialize(encoder: Encoder, value: LocalDate) = encoder.encodeString(value.format(formatter))
    override fun deserialize(decoder: Decoder): LocalDate = LocalDate.parse(decoder.decodeString(), formatter)
}

@Serializable
data class AlertRule(
    val id: String = UUID.randomUUID().toString(),
    val ticker: String,
    val rollingDays: Int,
    val thresholdPercent: Double,
    var lastTriggeredAt: Long = 0L,
    val isEnabled: Boolean = true
) {

    fun isActive(): Boolean = isEnabled
}