package org.chorus.block

import org.chorus.block.property.CommonBlockProperties
import org.chorus.item.ItemTool

class BlockPolishedTuffWall @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockWallBase(blockstate) {
    override val name: String
        get() = "Polished Tuff Wall"

    override val resistance: Double
        get() = 6.0

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
        val properties: BlockProperties = BlockProperties(
            BlockID.POLISHED_TUFF_WALL,
            CommonBlockProperties.WALL_CONNECTION_TYPE_EAST,
            CommonBlockProperties.WALL_CONNECTION_TYPE_NORTH,
            CommonBlockProperties.WALL_CONNECTION_TYPE_SOUTH,
            CommonBlockProperties.WALL_CONNECTION_TYPE_WEST,
            CommonBlockProperties.WALL_POST_BIT
        )

    }
}