package org.chorus.block

import org.chorus.block.property.CommonBlockProperties
import org.chorus.item.ItemTool

class BlockMossyStoneBrickDoubleSlab @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockDoubleSlabBase(blockstate) {
    override fun getSlabName() = "Mossy Stone Brick"

    override fun getSingleSlab() = BlockMossyStoneBrickSlab.properties.defaultState

    override val toolType: Int
        get() = ItemTool.TYPE_PICKAXE

    override val hardness: Double
        get() = 1.5

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.MOSSY_STONE_BRICK_DOUBLE_SLAB, CommonBlockProperties.MINECRAFT_VERTICAL_HALF)
    }
}