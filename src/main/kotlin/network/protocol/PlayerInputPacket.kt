package org.chorus_oss.chorus.network.protocol

import org.chorus_oss.chorus.network.connection.util.HandleByteBuf

class PlayerInputPacket : DataPacket() {
    var motionX: Float = 0f
    var motionY: Float = 0f
    var jumping: Boolean = false
    var sneaking: Boolean = false

    override fun pid(): Int {
        return ProtocolInfo.PLAYER_INPUT_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }

    companion object : PacketDecoder<PlayerInputPacket> {
        override fun decode(byteBuf: HandleByteBuf): PlayerInputPacket {
            val packet = PlayerInputPacket()

            packet.motionX = byteBuf.readFloatLE()
            packet.motionY = byteBuf.readFloatLE()
            packet.jumping = byteBuf.readBoolean()
            packet.sneaking = byteBuf.readBoolean()

            return packet
        }
    }
}
