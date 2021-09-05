package com.aliziane.alifordevcommunity

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.util.*

/**
 * Formats dates using [RFC 3339](https://www.ietf.org/rfc/rfc3339.txt), which is
 * formatted like 2015-09-26T18:23:50.250Z. This is a serializer for [Date].
 */
object Rfc3339DateSerializer : KSerializer<Date> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("Rfc3339Date", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Date) {
        encoder.encodeString(Iso8601Utils.format(value))
    }

    override fun deserialize(decoder: Decoder): Date = Iso8601Utils.parse(decoder.decodeString())
}