package org.chorus_oss.chorus.block

import org.chorus_oss.chorus.block.property.CommonBlockProperties
import org.chorus_oss.chorus.item.Item
import org.chorus_oss.chorus.item.ItemBlock
import org.chorus_oss.chorus.item.ItemTool

class BlockNetherBrickWall @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockWallBase(blockstate) {
    override val name: String
        get() = "Nether Brick Wall"

    override val resistance: Double
        get() = 6.0

    override val toolTier: Int
        get() = ItemTool.TIER_WOODEN



    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(
            BlockID.NETHER_BRICK_WALL,
            CommonBlockProperties.WALL_CONNECTION_TYPE_EAST,
            CommonBlockProperties.WALL_CONNECTION_TYPE_NORTH,
            CommonBlockProperties.WALL_CONNECTION_TYPE_SOUTH,
            CommonBlockProperties.WALL_CONNECTION_TYPE_WEST,
            CommonBlockProperties.WALL_POST_BIT
        )
    }
}
