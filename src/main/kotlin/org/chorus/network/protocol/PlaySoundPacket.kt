package org.chorus.network.protocol

import cn.nukkit.network.connection.util.HandleByteBuf
import lombok.*

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
class PlaySoundPacket : DataPacket() {
    var name: String? = null
    var x: Int = 0
    var y: Int = 0
    var z: Int = 0
    var volume: Float = 0f
    var pitch: Float = 0f

    override fun decode(byteBuf: HandleByteBuf) {
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeString(name!!)
        byteBuf.writeBlockVector3(this.x * 8, this.y * 8, this.z * 8)
        byteBuf.writeFloatLE(this.volume)
        byteBuf.writeFloatLE(this.pitch)
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.PLAY_SOUND_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
