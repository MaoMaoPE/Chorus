package org.chorus_oss.chorus.experimental.network.protocol.utils

import java.util.UUID
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
fun Uuid.Companion.from(value: UUID): Uuid {
    return Uuid.fromLongs(value.mostSignificantBits, value.leastSignificantBits)
}