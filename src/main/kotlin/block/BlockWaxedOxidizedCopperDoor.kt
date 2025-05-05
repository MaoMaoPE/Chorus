package org.chorus_oss.chorus.block

import org.chorus_oss.chorus.block.property.CommonBlockProperties
import org.chorus_oss.chorus.block.property.enums.OxidizationLevel

class BlockWaxedOxidizedCopperDoor @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockCopperDoorBase(blockstate) {
    override val oxidizationLevel
        get(): OxidizationLevel {
            return OxidizationLevel.OXIDIZED
        }

    override val isWaxed: Boolean
        get() = true

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(
            BlockID.WAXED_OXIDIZED_COPPER_DOOR,
            CommonBlockProperties.MINECRAFT_CARDINAL_DIRECTION,
            CommonBlockProperties.OPEN_BIT,
            CommonBlockProperties.UPPER_BLOCK_BIT,
            CommonBlockProperties.DOOR_HINGE_BIT
        )
    }
}