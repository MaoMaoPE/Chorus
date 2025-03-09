package org.chorus.block

import org.chorus.Player
import org.chorus.block.property.CommonBlockProperties
import org.chorus.item.*
import org.chorus.math.*

class BlockBorderBlock @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockWallBase(blockstate) {
    override val hardness: Double
        get() = -1.0

    override val resistance: Double
        get() = 18000000.0

    override val name: String
        get() = "Border Block"

    override fun canBePushed(): Boolean {
        return false
    }

    override fun canBePulled(): Boolean {
        return false
    }

    override fun canHarvestWithHand(): Boolean {
        return false
    }

    override fun place(
        item: Item,
        block: Block,
        target: Block,
        face: BlockFace,
        fx: Double,
        fy: Double,
        fz: Double,
        player: Player?
    ): Boolean {
        if (player != null && (!player.isCreative || !player.isOp)) {
            return false
        }
        return super.place(item, block, target, face, fx, fy, fz, player)
    }

    override fun isBreakable(vector: Vector3, layer: Int, face: BlockFace?, item: Item?, player: Player?): Boolean {
        if (player != null && (!player.isCreative || !player.isOp)) {
            return false
        }
        return super.isBreakable(vector, layer, face, item, player)
    }

    override fun getDrops(item: Item): Array<Item?>? {
        return Item.EMPTY_ARRAY
    }

    override fun recalculateBoundingBox(): AxisAlignedBB? {
        val aabb = super.recalculateBoundingBox()
        aabb!!.minY = Double.MIN_VALUE
        aabb.maxY = Double.MAX_VALUE
        return aabb
    }

    companion object {
        val properties: BlockProperties = BlockProperties(
            BORDER_BLOCK,
            CommonBlockProperties.WALL_CONNECTION_TYPE_EAST,
            CommonBlockProperties.WALL_CONNECTION_TYPE_NORTH,
            CommonBlockProperties.WALL_CONNECTION_TYPE_SOUTH,
            CommonBlockProperties.WALL_CONNECTION_TYPE_WEST,
            CommonBlockProperties.WALL_POST_BIT
        )
            get() = Companion.field
    }
}