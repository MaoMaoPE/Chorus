package org.chorus_oss.chorus.network.protocol

import org.chorus_oss.chorus.entity.data.EntityDataMap
import org.chorus_oss.chorus.network.connection.util.HandleByteBuf
import org.chorus_oss.chorus.network.protocol.types.PropertySyncData
import org.chorus_oss.chorus.utils.Binary
import org.chorus_oss.chorus.utils.Loggable

class SetEntityDataPacket : DataPacket() {
    @JvmField
    var eid: Long = 0

    var entityData: EntityDataMap = EntityDataMap()
    var syncedProperties: PropertySyncData = PropertySyncData(intArrayOf(), floatArrayOf())
    var frame: Long = 0

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeUnsignedVarLong(this.eid)
        byteBuf.writeBytes(Binary.writeEntityData(this.entityData))
        byteBuf.writePropertySyncData(this.syncedProperties)
        byteBuf.writeUnsignedVarLong(this.frame)
    }

    override fun pid(): Int {
        return ProtocolInfo.SET_ENTITY_DATA_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }

    companion object : Loggable
}
