package org.chorus_oss.chorus.network.protocol

import org.chorus_oss.chorus.nbt.tag.CompoundTag
import org.chorus_oss.chorus.network.connection.util.HandleByteBuf

data class CompressedBiomeDefinitionListPacket(
    val definitions: CompoundTag
) : DataPacket() {
    override fun encode(byteBuf: HandleByteBuf) {
        //TODO: Implement
    }

    override fun decode(byteBuf: HandleByteBuf) {
    }

    override fun pid(): Int {
        return ProtocolInfo.COMPRESSED_BIOME_DEFINITIONS_LIST
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }

    companion object {
        protected val COMPRESSED_INDICATOR: ByteArray = byteArrayOf(
            0xe4.toByte(),
            0x92.toByte(),
            0x3f,
            0x43,
            0x4f,
            0x4d,
            0x50,
            0x52,
            0x45,
            0x53,
            0x53,
            0x45,
            0x44
        ) // __?COMPRESSED
    }
}
