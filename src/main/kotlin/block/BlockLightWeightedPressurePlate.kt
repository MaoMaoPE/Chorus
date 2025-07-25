package org.chorus_oss.chorus.block

import org.chorus_oss.chorus.block.property.CommonBlockProperties
import org.chorus_oss.chorus.item.ItemTool
import kotlin.math.ceil
import kotlin.math.min

class BlockLightWeightedPressurePlate @JvmOverloads constructor(blockstate: BlockState = properties.defaultState) :
    BlockPressurePlateBase(blockstate) {
    init {
        this.onPitch = 0.90000004f
        this.offPitch = 0.75f
    }

    override val name: String
        get() = "Weighted Pressure Plate (Light)"

    override val hardness: Double
        get() = 0.5

    override val resistance: Double
        get() = 2.5

    override val toolType: Int
        get() = ItemTool.TYPE_PICKAXE

    override val toolTier: Int
        get() = ItemTool.TIER_WOODEN

    override fun computeRedstoneStrength(): Int {
        val count = min(
            level.getCollidingEntities(collisionBoundingBox!!).size.toDouble(),
            maxWeight.toDouble()
        ).toInt()

        if (count > 0) {
            val f = min(maxWeight.toDouble(), count.toDouble()).toFloat() / maxWeight.toFloat()
            return ceil(f * 15.0f).toInt()
        } else {
            return 0
        }
    }

    val maxWeight: Int
        get() = 15

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.LIGHT_WEIGHTED_PRESSURE_PLATE, CommonBlockProperties.REDSTONE_SIGNAL)
    }
}