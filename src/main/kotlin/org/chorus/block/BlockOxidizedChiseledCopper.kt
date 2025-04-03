package org.chorus.block

import org.chorus.block.property.enums.OxidizationLevel

class BlockOxidizedChiseledCopper @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockChiseledCopperBase(blockstate) {
    override val oxidizationLevel: OxidizationLevel
        get() = OxidizationLevel.OXIDIZED

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.OXIDIZED_CHISELED_COPPER)
    }
}