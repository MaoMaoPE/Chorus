package org.chorus.block

import org.chorus.block.property.CommonBlockProperties
import org.chorus.item.Item
import org.chorus.item.ItemBlock
import org.chorus.item.ItemTool

class BlockEndStoneBrickWall @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockWallBase(blockstate) {
    override val name: String
        get() = "End Stone Brick Wall"

    override val resistance: Double
        get() = 9.0

    override val hardness: Double
        get() = 3.0

    override val toolTier: Int
        get() = ItemTool.TIER_WOODEN

    override fun toItem(): Item {
        return ItemBlock(properties.defaultState.toBlock())
    }

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(
            BlockID.END_STONE_BRICK_WALL,
            CommonBlockProperties.WALL_CONNECTION_TYPE_EAST,
            CommonBlockProperties.WALL_CONNECTION_TYPE_NORTH,
            CommonBlockProperties.WALL_CONNECTION_TYPE_SOUTH,
            CommonBlockProperties.WALL_CONNECTION_TYPE_WEST,
            CommonBlockProperties.WALL_POST_BIT
        )
    }
}
