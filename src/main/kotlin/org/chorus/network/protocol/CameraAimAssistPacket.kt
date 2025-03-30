package org.chorus.network.protocol

import org.chorus.math.Vector2f
import org.chorus.network.connection.util.HandleByteBuf

data class CameraAimAssistPacket(
    val presetId: String,
    val viewAngle: Vector2f,
    val distance: Float,
    val targetMode: TargetMode,
    val action: Action,
) : DataPacket(), PacketEncoder {
    enum class TargetMode {
        ANGLE,
        DISTANCE,
        COUNT
    }

    enum class Action {
        SET,
        CLEAR,
        COUNT
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeString(this.presetId)
        byteBuf.writeVector2f(this.viewAngle)
        byteBuf.writeFloatLE(this.distance)
        byteBuf.writeByte(this.targetMode.ordinal)
        byteBuf.writeByte(this.action.ordinal)
    }

    override fun pid(): Int {
        return ProtocolInfo.CAMERA_AIM_ASSIST_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }

    companion object : PacketDecoder<CameraAimAssistPacket> {
        override fun decode(byteBuf: HandleByteBuf): CameraAimAssistPacket {
            return CameraAimAssistPacket(
                presetId = byteBuf.readString(),
                viewAngle = byteBuf.readVector2f(),
                distance = byteBuf.readFloatLE(),
                targetMode = TargetMode.entries[byteBuf.readUnsignedByte().toInt()],
                action = Action.entries[byteBuf.readUnsignedByte().toInt()]
            )
        }
    }
}
