package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockWaxedCutCopperStairs @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockCutCopperStairs(blockstate) {
    override val isWaxed: Boolean
        get() = true

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(
            BlockID.WAXED_CUT_COPPER_STAIRS,
            CommonBlockProperties.UPSIDE_DOWN_BIT,
            CommonBlockProperties.WEIRDO_DIRECTION
        )
    }
}