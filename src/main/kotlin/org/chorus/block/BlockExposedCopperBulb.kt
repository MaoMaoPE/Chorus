package org.chorus.block

import org.chorus.block.property.CommonBlockProperties
import org.chorus.block.property.enums.OxidizationLevel

class BlockExposedCopperBulb @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockCopperBulbBase(blockstate) {

    override val oxidizationLevel: OxidizationLevel
        get() = OxidizationLevel.EXPOSED

    override val lightLevel: Int
        get() = if (lit) 12 else 0

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.EXPOSED_COPPER_BULB, CommonBlockProperties.LIT, CommonBlockProperties.POWERED_BIT)
    }
}