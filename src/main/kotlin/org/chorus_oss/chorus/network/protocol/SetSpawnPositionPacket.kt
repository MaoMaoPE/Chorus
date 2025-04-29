package org.chorus_oss.chorus.network.protocol

import org.chorus_oss.chorus.network.connection.util.HandleByteBuf


class SetSpawnPositionPacket : DataPacket() {
    @JvmField
    var spawnType: Int = 0

    @JvmField
    var y: Int = 0

    @JvmField
    var z: Int = 0

    @JvmField
    var x: Int = 0

    @JvmField
    var dimension: Int = 0

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeVarInt(this.spawnType)
        byteBuf.writeBlockVector3(this.x, this.y, this.z)
        byteBuf.writeVarInt(dimension)
        byteBuf.writeBlockVector3(this.x, this.y, this.z)
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.SET_SPAWN_POSITION_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }

    companion object {
        const val TYPE_PLAYER_SPAWN: Int = 0
        const val TYPE_WORLD_SPAWN: Int = 1
    }
}
