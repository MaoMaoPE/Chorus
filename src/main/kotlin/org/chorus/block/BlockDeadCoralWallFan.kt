package org.chorus.block

import org.chorus.block.property.CommonBlockProperties
import org.chorus.block.property.type.IntPropertyType
import org.chorus.item.Item
import org.chorus.item.ItemBlock
import org.chorus.level.Level
import org.chorus.math.BlockFace

abstract class BlockDeadCoralWallFan(blockstate: BlockState) : BlockCoralFanDead(blockstate) {
    override val name: String
        get() = "Dead " + super.name + " Wall Fan"

    override fun onUpdate(type: Int): Int {
        return if (type == Level.BLOCK_UPDATE_RANDOM) {
            type
        } else {
            super.onUpdate(type)
        }
    }

    override var blockFace: BlockFace
        get() {
            val face =
                getPropertyValue<Int, IntPropertyType>(CommonBlockProperties.CORAL_DIRECTION)
            return when (face) {
                0 -> BlockFace.WEST
                1 -> BlockFace.EAST
                2 -> BlockFace.NORTH
                else -> BlockFace.SOUTH
            }
        }
        set(blockFace) {
            super.blockFace = blockFace
        }

    override val rootsFace: BlockFace
        get() = blockFace?.getOpposite()

    override fun toItem(): Item? {
        return ItemBlock(if (isDead) getDeadCoralFan() else this)
    }
}