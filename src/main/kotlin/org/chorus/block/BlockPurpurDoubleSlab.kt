package org.chorus.block

import org.chorus.block.property.CommonBlockProperties
import org.chorus.item.ItemTool

class BlockPurpurDoubleSlab @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockDoubleSlabBase(blockstate) {
    override fun getSlabName() = "Purpur"

    override fun getSingleSlab() = BlockPurpurSlab.properties.defaultState

    override val toolType: Int
        get() = ItemTool.TYPE_PICKAXE

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.PURPUR_DOUBLE_SLAB, CommonBlockProperties.MINECRAFT_VERTICAL_HALF)
    }
}