package org.chorus_oss.chorus.network.protocol

import org.chorus_oss.chorus.network.connection.util.HandleByteBuf

class PlayStatusPacket : DataPacket() {
    @JvmField
    var status: Int = 0

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeInt(this.status)
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.PLAY_STATUS_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }

    companion object {
        const val LOGIN_SUCCESS: Int = 0
        const val LOGIN_FAILED_CLIENT: Int = 1
        const val LOGIN_FAILED_SERVER: Int = 2
        const val PLAYER_SPAWN: Int = 3
        const val LOGIN_FAILED_INVALID_TENANT: Int = 4
        const val LOGIN_FAILED_VANILLA_EDU: Int = 5
        const val LOGIN_FAILED_EDU_VANILLA: Int = 6
        const val LOGIN_FAILED_SERVER_FULL: Int = 7
        const val LOGIN_FAILED_EDITOR_TO_VANILLA_MISMATCH: Int = 8
        const val LOGIN_FAILED_VANILLA_TO_EDITOR_MISMATCH: Int = 9
    }
}
