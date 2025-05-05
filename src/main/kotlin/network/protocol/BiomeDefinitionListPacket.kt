package org.chorus_oss.chorus.network.protocol

import org.chorus_oss.chorus.network.connection.util.HandleByteBuf

data class BiomeDefinitionListPacket(
    val biomeDefinitionData: ByteArray,
) : DataPacket(), PacketEncoder {
    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeBytes(biomeDefinitionData)
    }

    override fun pid(): Int {
        return ProtocolInfo.BIOME_DEFINITION_LIST_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BiomeDefinitionListPacket

        return biomeDefinitionData.contentEquals(other.biomeDefinitionData)
    }

    override fun hashCode(): Int {
        return biomeDefinitionData.contentHashCode()
    }
}
