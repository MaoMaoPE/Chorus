package org.chorus.block

import org.chorus.block.property.enums.OxidizationLevel

class BlockWaxedOxidizedCopperGrate @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockCopperGrateBase(blockstate) {
    override val oxidizationLevel
        get(): OxidizationLevel {
            return OxidizationLevel.OXIDIZED
        }

    override val isWaxed: Boolean
        get() = true

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.WAXED_OXIDIZED_COPPER_GRATE)
    }
}