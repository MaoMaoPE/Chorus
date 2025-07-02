package org.chorus_oss.chorus.network.protocol

import org.chorus_oss.chorus.network.ProtocolInfo
import org.chorus_oss.chorus.network.connection.util.HandleByteBuf

class ContainerSetDataPacket(
    val containerID: Byte,
    val property: Int,
    val value: Int,
) : DataPacket() {
    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeByte(this.containerID.toInt())
        byteBuf.writeVarInt(this.property)
        byteBuf.writeVarInt(this.value)
    }

    override fun pid(): Int {
        return ProtocolInfo.CONTAINER_SET_DATA_PACKET
    }

    companion object {
        const val PROPERTY_FURNACE_TICK_COUNT: Int = 0
        const val PROPERTY_FURNACE_LIT_TIME: Int = 1
        const val PROPERTY_FURNACE_LIT_DURATION: Int = 2

        // TODO: check property 3
        const val PROPERTY_FURNACE_FUEL_AUX: Int = 4

        const val PROPERTY_BREWING_STAND_BREW_TIME: Int = 0
        const val PROPERTY_BREWING_STAND_FUEL_AMOUNT: Int = 1
        const val PROPERTY_BREWING_STAND_FUEL_TOTAL: Int = 2
    }
}
