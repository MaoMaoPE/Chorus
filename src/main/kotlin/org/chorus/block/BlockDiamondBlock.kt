package org.chorus.block

import org.chorus.item.ItemTool

class BlockDiamondBlock @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockSolid(blockstate) {
    override val hardness: Double
        get() = 5.0

    override val resistance: Double
        get() = 30.0

    override val toolType: Int
        get() = ItemTool.TYPE_PICKAXE

    override val name: String
        get() = "Diamond Block"

    override val toolTier: Int
        get() = ItemTool.TIER_IRON

    override fun canHarvestWithHand(): Boolean {
        return false
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.DIAMOND_BLOCK)

    }
}