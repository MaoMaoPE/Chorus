package org.chorus_oss.chorus.network.protocol

import org.chorus_oss.chorus.inventory.SpecialWindowId
import org.chorus_oss.chorus.network.ProtocolInfo
import org.chorus_oss.chorus.network.connection.util.HandleByteBuf

class PlayerHotbarPacket : DataPacket() {
    var selectedHotbarSlot: Int = 0
    var windowId: Int = SpecialWindowId.PLAYER.id
    var selectHotbarSlot: Boolean = true

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeUnsignedVarInt(this.selectedHotbarSlot)
        byteBuf.writeByte(windowId.toByte().toInt())
        byteBuf.writeBoolean(this.selectHotbarSlot)
    }

    override fun pid(): Int {
        return ProtocolInfo.PLAYER_HOTBAR_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }

    companion object : PacketDecoder<PlayerHotbarPacket> {
        override fun decode(byteBuf: HandleByteBuf): PlayerHotbarPacket {
            val packet = PlayerHotbarPacket()

            packet.selectedHotbarSlot = byteBuf.readUnsignedVarInt()
            packet.windowId = byteBuf.readByte().toInt()
            packet.selectHotbarSlot = byteBuf.readBoolean()

            return packet
        }
    }
}
