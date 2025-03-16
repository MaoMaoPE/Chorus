package org.chorus.block

import org.chorus.Player
import org.chorus.block.property.enums.OxidizationLevel
import org.chorus.item.*
import org.chorus.math.BlockFace
import org.chorus.registry.Registries

abstract class BlockCopperBase(blockState: BlockState?) : BlockSolid(blockState), Oxidizable, Waxable {
    override val hardness: Double
        get() = 3.0

    override val resistance: Double
        get() = 6.0

    override val toolType: Int
        get() = ItemTool.TYPE_PICKAXE

    override val toolTier: Int
        get() = ItemTool.TIER_STONE

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

    override fun canHarvestWithHand(): Boolean {
        return false
    }

    override fun getBlockWithOxidizationLevel(oxidizationLevel: OxidizationLevel): Block {
        return Registries.BLOCK.getBlockProperties(getCopperId(isWaxed, oxidizationLevel)).defaultState.toBlock()
    }

    override fun setOxidizationLevel(oxidizationLevel: OxidizationLevel): Boolean {
        if (this.oxidizationLevel == oxidizationLevel) {
            return true
        }
        return level.setBlock(this.position, get(getCopperId(isWaxed, oxidizationLevel)))
    }

    override fun setWaxed(waxed: Boolean): Boolean {
        if (isWaxed == waxed) {
            return true
        }
        return level.setBlock(
            this.position, get(
                getCopperId(
                    waxed,
                    oxidizationLevel
                )
            )
        )
    }

    override val isWaxed: Boolean
        get() =  false

    protected open fun getCopperId(waxed: Boolean, oxidizationLevel: OxidizationLevel?): String {
        if (oxidizationLevel == null) {
            return id
        }
        return when (oxidizationLevel) {
            OxidizationLevel.UNAFFECTED -> if (waxed) BlockID.WAXED_COPPER else BlockID.COPPER_BLOCK
            OxidizationLevel.EXPOSED -> if (waxed) BlockID.WAXED_EXPOSED_COPPER else BlockID.EXPOSED_COPPER
            OxidizationLevel.WEATHERED -> if (waxed) BlockID.WAXED_WEATHERED_COPPER else BlockID.WEATHERED_COPPER
            OxidizationLevel.OXIDIZED -> if (waxed) BlockID.WAXED_OXIDIZED_COPPER else BlockID.OXIDIZED_COPPER
        }
    }
}
