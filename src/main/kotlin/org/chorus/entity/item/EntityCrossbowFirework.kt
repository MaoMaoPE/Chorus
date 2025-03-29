package org.chorus.entity.item

import org.chorus.Server
import org.chorus.level.format.IChunk
import org.chorus.nbt.tag.CompoundTag
import org.chorus.network.protocol.EntityEventPacket
import java.util.*
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.sqrt

class EntityCrossbowFirework(chunk: IChunk?, nbt: CompoundTag) : EntityFireworksRocket(chunk, nbt) {
    private val lifetime: Int
    private var fireworkAge: Int = 0

    init {
        this.lifetime = 10 + RANDOM.nextInt(13)
    }

    override fun onUpdate(currentTick: Int): Boolean {
        if (this.closed) {
            return false
        } else {
            val tickDiff: Int = currentTick - this.lastUpdate
            if (tickDiff <= 0 && !this.justCreated) {
                return true
            } else {
                this.lastUpdate = currentTick
                var hasUpdate: Boolean = this.entityBaseTick(tickDiff)
                if (this.isAlive()) {
                    motion.x *= 1.15
                    motion.z *= 1.15
                    this.move(motion.x, motion.y, motion.z)
                    this.updateMovement()
                    val f: Float = sqrt(motion.x * motion.x + motion.z * motion.z).toFloat()
                    rotation.yaw = (atan2(motion.x, motion.z) * 57.29577951308232).toFloat().toDouble()
                    rotation.pitch = (atan2(motion.y, f.toDouble()) * 57.29577951308232).toFloat().toDouble()
                    if (this.fireworkAge == 0) {
                        level!!.addLevelSoundEvent(this.position, 56)
                    }

                    ++this.fireworkAge
                    hasUpdate = true
                    if (this.fireworkAge >= this.lifetime) {
                        val pk: EntityEventPacket = EntityEventPacket()
                        pk.data = 0
                        pk.event = 25
                        pk.eid = this.getRuntimeID()
                        level!!.addLevelSoundEvent(this.position, 58, -1, 72)
                        Server.broadcastPacket(getViewers().values, pk)
                        this.kill()
                    }
                }
                return hasUpdate || !this.onGround || abs(motion.x) > 1.0E-5 || abs(
                    motion.y
                ) > 1.0E-5 || abs(motion.z) > 1.0E-5
            }
        }
    }

    companion object {
        private val RANDOM: Random = Random()
    }
}
