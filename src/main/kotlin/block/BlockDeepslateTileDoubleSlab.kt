package org.chorus_oss.chorus.block

import org.chorus_oss.chorus.block.property.CommonBlockProperties
import org.chorus_oss.chorus.item.ItemTool

class BlockDeepslateTileDoubleSlab @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockDoubleSlabBase(blockstate) {

    override fun getSlabName() = "Deepslate Tile"

    override val hardness: Double
        get() = 3.5

    override val resistance: Double
        get() = 6.0

    override fun canHarvestWithHand(): Boolean {
        return false
    }

    override val toolTier: Int
        get() = ItemTool.TIER_WOODEN

    override val toolType: Int
        get() = ItemTool.TYPE_PICKAXE

    override fun getSingleSlab() = BlockDeepslateTileSlab.properties.defaultState

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.DEEPSLATE_TILE_DOUBLE_SLAB, CommonBlockProperties.MINECRAFT_VERTICAL_HALF)
    }
}