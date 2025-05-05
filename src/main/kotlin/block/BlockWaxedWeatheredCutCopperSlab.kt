package org.chorus_oss.chorus.block

import org.chorus_oss.chorus.block.property.CommonBlockProperties

class BlockWaxedWeatheredCutCopperSlab @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockWeatheredCutCopperSlab(blockstate, BlockID.WAXED_WEATHERED_DOUBLE_CUT_COPPER_SLAB) {
    override val isWaxed: Boolean
        get() = true

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.WAXED_WEATHERED_CUT_COPPER_SLAB, CommonBlockProperties.MINECRAFT_VERTICAL_HALF)
    }
}