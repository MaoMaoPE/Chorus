package org.chorus_oss.chorus.block

import org.chorus_oss.chorus.block.property.CommonBlockProperties
import org.chorus_oss.chorus.item.ItemTool

class BlockBrickStairs @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockStairs(blockstate) {
    override val hardness: Double
        get() = 2.0

    override val resistance: Double
        get() = 30.0

    override val toolType: Int
        get() = ItemTool.TYPE_PICKAXE

    override val toolTier: Int
        get() = ItemTool.TIER_WOODEN

    override val name: String
        get() = "Brick Stairs"

    override fun canHarvestWithHand(): Boolean {
        return false
    }

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties =
            BlockProperties(
                BlockID.BRICK_STAIRS,
                CommonBlockProperties.UPSIDE_DOWN_BIT,
                CommonBlockProperties.WEIRDO_DIRECTION
            )
    }
}