package org.chorus_oss.chorus.block

import org.chorus_oss.chorus.block.property.CommonBlockProperties
import org.chorus_oss.chorus.item.ItemTool

class BlockDarkPrismarineDoubleSlab @JvmOverloads constructor(blockstate: BlockState = properties.defaultState) :
    BlockDoubleSlabBase(blockstate) {

    override fun getSlabName(): String {
        return "Dark Prismarine"
    }

    override fun getSingleSlab(): BlockState {
        return BlockDarkPrismarineSlab.properties.defaultState
    }

    override val toolType: Int
        get() = ItemTool.TYPE_PICKAXE

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.DARK_PRISMARINE_DOUBLE_SLAB, CommonBlockProperties.MINECRAFT_VERTICAL_HALF)
    }
}