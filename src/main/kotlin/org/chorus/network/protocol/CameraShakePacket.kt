package org.chorus.network.protocol

import org.chorus.network.connection.util.HandleByteBuf


class CameraShakePacket(
    val intensity: Float,
    val seconds: Float,
    val shakeType: CameraShakeType,
    val shakeAction: CameraShakeAction,
) : DataPacket(), PacketEncoder {
    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeFloatLE(this.intensity)
        byteBuf.writeFloatLE(this.seconds)
        byteBuf.writeByte(shakeType.ordinal)
        byteBuf.writeByte(shakeAction.ordinal)
    }

    enum class CameraShakeAction {
        ADD,
        STOP
    }

    enum class CameraShakeType {
        POSITIONAL,
        ROTATIONAL
    }

    override fun pid(): Int {
        return ProtocolInfo.CAMERA_SHAKE_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
