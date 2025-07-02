package org.chorus_oss.chorus.network.protocol

import org.chorus_oss.chorus.network.ProtocolInfo
import org.chorus_oss.chorus.network.connection.util.HandleByteBuf
import org.chorus_oss.chorus.network.protocol.types.BlockSyncType


class UpdateBlockSyncedPacket : UpdateBlockPacket() {
    var actorUniqueId: Long = 0
    var updateType: BlockSyncType? = null

    override fun encode(byteBuf: HandleByteBuf) {
        super.encode(byteBuf)
        byteBuf.writeUnsignedVarLong(actorUniqueId)
        byteBuf.writeUnsignedVarLong(updateType!!.ordinal.toLong())
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.UPDATE_BLOCK_SYNCED_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
