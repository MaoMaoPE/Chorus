package org.chorus.block

import org.chorus.Player
import org.chorus.block.property.enums.OxidizationLevel
import org.chorus.item.Item
import org.chorus.item.ItemTool
import org.chorus.math.BlockFace

abstract class BlockStairsCopperBase(blockstate: BlockState) : BlockStairs(blockstate), Waxable,
    Oxidizable {
    override fun onActivate(
        item: Item,
        player: Player?,
        blockFace: BlockFace,
        fx: Float,
        fy: Float,
        fz: Float
    ): Boolean {
        return super<Waxable>.onActivate(item, player, blockFace, fx, fy, fz)
                || super<Oxidizable>.onActivate(item, player, blockFace, fx, fy, fz)
    }

    override fun onUpdate(type: Int): Int {
        return super<Oxidizable>.onUpdate(type)
    }

    override fun canBeActivated(): Boolean {
        return true
    }

    override val hardness: Double
        get() = 3.0

    override val resistance: Double
        get() = 6.0

    override val toolType: Int
        get() = ItemTool.TYPE_PICKAXE

    override val toolTier: Int
        get() = ItemTool.TIER_STONE

    override fun canHarvestWithHand(): Boolean {
        return false
    }

    override fun getBlockWithOxidizationLevel(oxidizationLevel: OxidizationLevel): Block {
        return get(getCopperId(isWaxed, oxidizationLevel)!!).setPropertyValues(propertyValues)
    }

    override fun setOxidizationLevel(oxidizationLevel: OxidizationLevel): Boolean {
        if (this.oxidizationLevel == oxidizationLevel) {
            return true
        }

        return level.setBlock(
            this.position,
            get(getCopperId(isWaxed, oxidizationLevel)!!).setPropertyValues(propertyValues)
        )
    }

    override fun setWaxed(waxed: Boolean): Boolean {
        if (isWaxed == waxed) {
            return true
        }
        return level.setBlock(
            this.position, get(
                getCopperId(
                    isWaxed,
                    oxidizationLevel
                )!!
            ).setPropertyValues(propertyValues)
        )
    }

    override val isWaxed: Boolean
        get() = false


    protected abstract fun getCopperId(waxed: Boolean, oxidizationLevel: OxidizationLevel?): String?
}
