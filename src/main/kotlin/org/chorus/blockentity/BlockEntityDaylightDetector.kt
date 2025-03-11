package org.chorus.blockentity

import org.chorus.block.BlockDaylightDetector
import org.chorus.block.BlockID
import org.chorus.level.format.IChunk
import org.chorus.nbt.tag.CompoundTag

class BlockEntityDaylightDetector(chunk: IChunk, nbt: CompoundTag) : BlockEntity(chunk, nbt) {
    override fun initBlockEntity() {
        scheduleUpdate()
    }

    override val isBlockEntityValid: Boolean
        get() {
            val id = levelBlock.id
            return id === BlockID.DAYLIGHT_DETECTOR || id === BlockID.DAYLIGHT_DETECTOR_INVERTED
        }

    override fun onUpdate(): Boolean {
        if (!Server.instance.settings.levelSettings().enableRedstone()) {
            return false
        }
        if (level.currentTick % 20 != 0L) {
            //阳光传感器每20gt更新一次
            return true
        }
        val block = levelBlock
        if (block is BlockDaylightDetector) {
            block.updatePower()
            return true
        } else {
            return false
        }
    }
}
