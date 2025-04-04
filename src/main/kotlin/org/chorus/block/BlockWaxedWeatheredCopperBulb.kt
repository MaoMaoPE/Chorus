package org.chorus.block

import org.chorus.block.property.CommonBlockProperties
import org.chorus.block.property.enums.OxidizationLevel

class BlockWaxedWeatheredCopperBulb @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockCopperBulbBase(blockstate) {
    override val oxidizationLevel
        get(): OxidizationLevel {
            return OxidizationLevel.WEATHERED
        }

    override val isWaxed: Boolean
        get() = true

    override val lightLevel: Int
        get() = if (lit) 8 else 0

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(
            BlockID.WAXED_WEATHERED_COPPER_BULB,
            CommonBlockProperties.LIT,
            CommonBlockProperties.POWERED_BIT
        )
    }
}