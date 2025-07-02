package org.chorus_oss.chorus.network.protocol

import org.chorus_oss.chorus.network.ProtocolInfo
import org.chorus_oss.chorus.network.connection.util.HandleByteBuf


class NPCRequestPacket : DataPacket() {
    var entityRuntimeId: Long = 0
    var requestType: RequestType = RequestType.SET_SKIN
    var data: String = ""
    var skinType: Int = 0
    var sceneName: String = ""

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeActorRuntimeID(this.entityRuntimeId)
        byteBuf.writeByte(requestType.ordinal.toByte().toInt())
        byteBuf.writeString(this.data)
        byteBuf.writeByte(skinType.toByte().toInt())
        byteBuf.writeString(this.sceneName)
    }

    enum class RequestType {
        SET_ACTIONS,
        EXECUTE_ACTION,
        EXECUTE_CLOSING_COMMANDS,
        SET_NAME,
        SET_SKIN,
        SET_INTERACTION_TEXT,
        EXECUTE_OPENING_COMMANDS
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.NPC_REQUEST_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }

    companion object : PacketDecoder<NPCRequestPacket> {
        override fun decode(byteBuf: HandleByteBuf): NPCRequestPacket {
            val packet = NPCRequestPacket()

            packet.entityRuntimeId = byteBuf.readActorRuntimeID()
            packet.requestType = RequestType.entries[byteBuf.readByte().toInt()]
            packet.data = byteBuf.readString()
            packet.skinType = byteBuf.readByte().toInt()
            packet.sceneName = byteBuf.readString()

            return packet
        }
    }
}
