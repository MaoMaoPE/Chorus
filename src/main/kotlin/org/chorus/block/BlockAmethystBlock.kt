package org.chorus.block

import org.chorus.item.ItemTool

class BlockAmethystBlock @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockSolid(blockstate) {
    override val name: String
        get() = "Amethyst Block"

    override val hardness: Double
        get() = 1.5

    override val resistance: Double
        get() = 1.5

    override val toolType: Int
        get() = ItemTool.TYPE_PICKAXE

    override val toolTier: Int
        get() = ItemTool.TIER_IRON

    override fun canHarvestWithHand(): Boolean {
        return false
    }

    override val isLavaResistant: Boolean
        get() = true

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.AMETHYST_BLOCK)
    }
}