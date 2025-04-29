package org.chorus_oss.chorus.block

import org.chorus_oss.chorus.block.property.CommonBlockProperties
import org.chorus_oss.chorus.item.ItemTool

class BlockPolishedBlackstoneBrickSlab @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockSlab(blockstate, BlockID.POLISHED_BLACKSTONE_BRICK_DOUBLE_SLAB) {
    override fun getSlabName(): String {
        return "Polished Blackstone"
    }

    override fun isSameType(slab: BlockSlab): Boolean {
        return id == slab.id
    }

    override fun canHarvestWithHand(): Boolean {
        return false
    }

    override val toolTier: Int
        get() = ItemTool.TIER_WOODEN

    override val toolType: Int
        get() = ItemTool.TYPE_PICKAXE

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.POLISHED_BLACKSTONE_BRICK_SLAB, CommonBlockProperties.MINECRAFT_VERTICAL_HALF)
    }
}