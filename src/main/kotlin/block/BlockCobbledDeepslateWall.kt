package org.chorus_oss.chorus.block

import org.chorus_oss.chorus.block.property.CommonBlockProperties
import org.chorus_oss.chorus.item.ItemTool

class BlockCobbledDeepslateWall @JvmOverloads constructor(blockstate: BlockState = properties.defaultState) :
    BlockWallBase(blockstate) {
    override val name: String
        get() = "Cobbled Deepslate Wall"

    override val resistance: Double
        get() = 6.0

    override val hardness: Double
        get() = 3.5

    override val toolTier: Int
        get() = ItemTool.TIER_WOODEN

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(
            BlockID.COBBLED_DEEPSLATE_WALL,
            CommonBlockProperties.WALL_CONNECTION_TYPE_EAST,
            CommonBlockProperties.WALL_CONNECTION_TYPE_NORTH,
            CommonBlockProperties.WALL_CONNECTION_TYPE_SOUTH,
            CommonBlockProperties.WALL_CONNECTION_TYPE_WEST,
            CommonBlockProperties.WALL_POST_BIT
        )
    }
}