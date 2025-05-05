package org.chorus_oss.chorus.level.particle

import org.chorus_oss.chorus.block.Block
import org.chorus_oss.chorus.math.Vector3
import org.chorus_oss.chorus.network.protocol.DataPacket
import org.chorus_oss.chorus.network.protocol.LevelEventPacket

class DestroyBlockParticle(pos: Vector3, block: Block) : Particle(pos.x, pos.y, pos.z) {
    private val data: Int

    init {
        this.data = block.runtimeId
    }

    override fun encode(): Array<DataPacket> {
        val pk = LevelEventPacket()
        pk.evid = LevelEventPacket.EVENT_PARTICLE_DESTROY_BLOCK
        pk.x = x.toFloat()
        pk.y = y.toFloat()
        pk.z = z.toFloat()
        pk.data = this.data

        return arrayOf(pk)
    }
}
