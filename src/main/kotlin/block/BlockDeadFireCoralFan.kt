package org.chorus_oss.chorus.block

import org.chorus_oss.chorus.block.property.CommonBlockProperties

class BlockDeadFireCoralFan(blockState: BlockState = properties.defaultState) : BlockCoralFanDead(blockState) {
    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.DEAD_FIRE_CORAL_FAN, CommonBlockProperties.CORAL_FAN_DIRECTION)
    }
}