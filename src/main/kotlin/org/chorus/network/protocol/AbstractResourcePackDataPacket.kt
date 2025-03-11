package org.chorus.network.protocol

import org.chorus.network.connection.util.HandleByteBuf
import org.chorus.utils.version.Version
import java.util.*

abstract class AbstractResourcePackDataPacket : DataPacket() {
    abstract var packVersion: Version?

    abstract var packId: UUID?

    protected fun decodePackInfo(byteBuf: HandleByteBuf) {
        val packInfo = byteBuf.readString()
        val packInfoParts = packInfo.split("_", ignoreCase = false, limit = 2)
        packId = try {
            UUID.fromString(packInfoParts[0])
        } catch (exception: IllegalArgumentException) {
            null
        }
        packVersion = if (packInfoParts.size > 1) Version(packInfoParts[1]) else null
    }

    protected fun encodePackInfo(byteBuf: HandleByteBuf) {
        val packId = packId
        val packVersion = packVersion
        var packInfo = packId?.toString() ?: UUID(0, 0).toString()
        if (packVersion != null) {
            packInfo += "_$packVersion"
        }
        byteBuf.writeString(packInfo)
    }
}
