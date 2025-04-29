package org.chorus_oss.chorus.block

import org.chorus_oss.chorus.item.ItemTool

class BlockPolishedGranite @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockSolid(blockstate) {
    override val hardness: Double
        get() = 1.5

    override val resistance: Double
        get() = 6.0

    override val toolType: Int
        get() = ItemTool.TYPE_PICKAXE

    override val toolTier: Int
        get() = ItemTool.TIER_WOODEN

    override fun canHarvestWithHand(): Boolean {
        return false
    }

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.POLISHED_GRANITE)
    }
}