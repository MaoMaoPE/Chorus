package org.chorus.block

import org.chorus.block.property.CommonBlockProperties
import org.chorus.block.property.enums.OxidizationLevel

class BlockWaxedOxidizedCopperBulb @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockCopperBulbBase(blockstate) {
    override val oxidizationLevel
        get(): OxidizationLevel {
            return OxidizationLevel.OXIDIZED
        }

    override val isWaxed: Boolean
        get() = true

    override val lightLevel: Int
        get() = if (lit) 4 else 0

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(
            BlockID.WAXED_OXIDIZED_COPPER_BULB,
            CommonBlockProperties.LIT,
            CommonBlockProperties.POWERED_BIT
        )
    }
}