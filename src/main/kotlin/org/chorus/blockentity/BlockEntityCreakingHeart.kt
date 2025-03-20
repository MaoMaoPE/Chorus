package org.chorus.blockentity

import org.chorus.block.*
import org.chorus.entity.Entity
import org.chorus.entity.mob.monster.EntityCreaking
import org.chorus.event.entity.CreatureSpawnEvent
import org.chorus.level.Level
import org.chorus.level.Locator
import org.chorus.level.Sound
import org.chorus.level.format.IChunk
import org.chorus.math.BlockFace
import org.chorus.nbt.tag.CompoundTag
import org.chorus.utils.*


class BlockEntityCreakingHeart(chunk: IChunk, nbt: CompoundTag) : BlockEntitySpawnable(chunk, nbt) {

    var linkedCreaking: EntityCreaking? = null

    var spawnRangeHorizontal: Double = 16.5
    var spawnRangeVertical: Double = 8.5

    init {
        movable = true
    }

    override fun initBlockEntity() {
        super.initBlockEntity()
        if (level.dimension == Level.DIMENSION_OVERWORLD) {
            scheduleUpdate()
        }
    }

    override val isBlockEntityValid: Boolean
        get() = this.block.id == Block.CREAKING_HEART

    override var name: String
        get() = "Creaking Heart"
        set(name) {
            super.name = name
        }

    val heart: BlockCreakingHeart
        get() = block as BlockCreakingHeart

    fun setLinkedCreaking(creaking: EntityCreaking?) {
        if (linkedCreaking != null) {
            linkedCreaking.setCreakingHeart(null)
        }
        creaking?.setCreakingHeart(this)
        linkedCreaking = creaking
    }

    override fun onUpdate(): Boolean {
        if (level.tick % 40 == 0 && isBlockEntityValid && heart.isActive) {
            level.addSound(this.position, Sound.BLOCK_CREAKING_HEART_AMBIENT)
        }
        if ((linkedCreaking == null || !linkedCreaking.isAlive()) && isBlockEntityValid && heart.isActive && (!level.isDay || level.isRaining || level.isThundering)) {
            val pos = Locator(
                position.x + Utils.rand(-this.spawnRangeHorizontal, this.spawnRangeHorizontal),
                position.y,
                position.z + Utils.rand(-this.spawnRangeHorizontal, this.spawnRangeHorizontal),
                this.level
            )

            var i = -spawnRangeVertical
            height@ while (i < spawnRangeVertical) {
                val newPos = pos.add(0.0, i, 0.0)
                if (!newPos.levelBlock.isAir) {
                    for (j in 1..2) {
                        if (!getSide(BlockFace.UP, j).levelBlock.isAir) {
                            i++
                            continue@height
                        }
                    }
                    pos.position.y = i
                    break
                }
                i++
            }
            val ent = Entity.createEntity(Entity.CREAKING, pos)
            if (ent != null) {
                val ev =
                    CreatureSpawnEvent(ent.networkId, pos, CompoundTag(), CreatureSpawnEvent.SpawnReason.CREAKING_HEART)
                Server.instance.pluginManager.callEvent(ev)
                if (ev.isCancelled) {
                    ent.close()
                } else {
                    setLinkedCreaking(ent as EntityCreaking)
                    level.addSound(this.position, Sound.BLOCK_CREAKING_HEART_MOB_SPAWN, 1f, 1f)
                    ent.spawnToAll()
                }
            }
        }
        return true
    }

    override fun onBreak(isSilkTouch: Boolean) {
        if (linkedCreaking != null) {
            linkedCreaking.kill()
        }
        super.onBreak(isSilkTouch)
    }
}
