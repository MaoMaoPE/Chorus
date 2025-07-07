package org.chorus_oss.chorus.network.protocol

import org.chorus_oss.chorus.network.ProtocolInfo
import org.chorus_oss.chorus.network.connection.util.HandleByteBuf
import org.chorus_oss.chorus.network.protocol.types.AbilityType
import org.chorus_oss.chorus.network.protocol.types.PlayerAbility


class RequestAbilityPacket : DataPacket() {
    var ability: PlayerAbility? = null
    var type: AbilityType? = null
    var boolValue: Boolean = false
    var floatValue: Float = 0f

    override fun pid(): Int {
        return ProtocolInfo.REQUEST_ABILITY_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }

    companion object : PacketDecoder<RequestAbilityPacket> {
        override fun decode(byteBuf: HandleByteBuf): RequestAbilityPacket {
            val packet = RequestAbilityPacket()

            packet.ability = ABILITIES[byteBuf.readVarInt()]
            packet.type = ABILITY_TYPES[byteBuf.readByte().toInt()]
            packet.boolValue = byteBuf.readBoolean()
            packet.floatValue = byteBuf.readFloatLE()

            return packet
        }

        val ABILITIES: Array<PlayerAbility> = PlayerAbility.entries.toTypedArray()
        val ABILITY_TYPES: Array<AbilityType> = AbilityType.entries.toTypedArray()
    }
}
