package org.chorus.block

import org.chorus.block.property.enums.OxidizationLevel

open class BlockExposedCopper @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockCopperBlock(blockstate) {
    override val oxidizationLevel: OxidizationLevel
        get() = OxidizationLevel.EXPOSED

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.EXPOSED_COPPER)
    }
}