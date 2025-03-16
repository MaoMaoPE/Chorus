package org.chorus.block

import org.chorus.block.property.CommonBlockProperties
import org.chorus.item.ItemTool

class BlockTuffBrickSlab @JvmOverloads constructor(blockstate: BlockState = Companion.properties.getDefaultState()) :
    BlockSlab(blockstate, BlockID.TUFF_BRICK_DOUBLE_SLAB) {
    override val slabName: String
        get() = "Tuff Brick"

    override val name: String
        get() = "Tuff Brick Slab"

    override val resistance: Double
        get() = 6.0

    override fun isSameType(slab: BlockSlab): Boolean {
        return id == slab.id
    }

    override val hardness: Double
        get() = 1.5

    override val toolTier: Int
        get() = ItemTool.TIER_WOODEN

    override val toolType: Int
        get() = ItemTool.TYPE_PICKAXE

    override fun canHarvestWithHand(): Boolean {
        return false
    }

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.TUFF_BRICK_SLAB, CommonBlockProperties.MINECRAFT_VERTICAL_HALF)

    }
}