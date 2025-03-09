package cn.nukkit.block

import cn.nukkit.Player
import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.block.property.type.BooleanPropertyType
import cn.nukkit.event.Event.isCancelled
import cn.nukkit.item.*
import cn.nukkit.level.Level
import cn.nukkit.math.BlockFace
import cn.nukkit.math.Vector3.equals
import cn.nukkit.registry.Registries
import cn.nukkit.utils.RedstoneComponent

abstract class BlockCopperBulbBase(blockState: BlockState?) : BlockSolid(blockState), RedstoneComponent,
    Oxidizable, Waxable {
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
        blockFace: BlockFace?,
        fx: Float,
        fy: Float,
        fz: Float
    ): Boolean {
        return super<Waxable>.onActivate(item, player, blockFace, fx, fy, fz)
                || super<Oxidizable>.onActivate(item, player, blockFace, fx, fy, fz)
    }

    override fun canBeActivated(): Boolean {
        return true
    }

    override fun onUpdate(type: Int): Int {
        super<Oxidizable>.onUpdate(type)

        if (type == Level.BLOCK_UPDATE_REDSTONE) {
            val ev: RedstoneUpdateEvent = RedstoneUpdateEvent(this)
            level.server.pluginManager.callEvent(ev)

            if (ev.isCancelled) {
                return 0
            }

            if (isGettingPower) {
                this.lit = !(lit)

                this.powered = true
                level.setBlock(this.position, this, true, true)
                return 1
            }

            if (powered) {
                this.powered = false
                level.setBlock(this.position, this, true, true)
                return 1
            }
        }
        return 0
    }

    override fun hasComparatorInputOverride(): Boolean {
        return true
    }

    override val comparatorInputOverride: Int
        get() = if (lit) 15 else 0

    var lit: Boolean
        get() = getPropertyValue<Boolean, BooleanPropertyType>(CommonBlockProperties.LIT)
        set(lit) {
            this.setPropertyValue<Boolean, BooleanPropertyType>(CommonBlockProperties.LIT, lit)
        }

    var powered: Boolean
        get() = getPropertyValue<Boolean, BooleanPropertyType>(CommonBlockProperties.POWERED_BIT)
        set(powered) {
            this.setPropertyValue<Boolean, BooleanPropertyType>(
                CommonBlockProperties.POWERED_BIT,
                powered
            )
        }

    override fun getBlockWithOxidizationLevel(oxidizationLevel: OxidizationLevel): Block {
        return Registries.BLOCK.getBlockProperties(getCopperId(isWaxed, oxidizationLevel)).defaultState.toBlock()
    }

    override fun setOxidizationLevel(oxidizationLevel: OxidizationLevel): Boolean {
        if (oxidizationLevel == oxidizationLevel) {
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

    override fun isWaxed(): Boolean {
        return false
    }

    protected fun getCopperId(waxed: Boolean, oxidizationLevel: OxidizationLevel?): String {
        if (oxidizationLevel == null) {
            return id
        }
        return when (oxidizationLevel) {
            OxidizationLevel.UNAFFECTED -> if (waxed) WAXED_COPPER_BULB else COPPER_BULB
            OxidizationLevel.EXPOSED -> if (waxed) WAXED_EXPOSED_COPPER_BULB else EXPOSED_COPPER_BULB
            OxidizationLevel.WEATHERED -> if (waxed) WAXED_WEATHERED_COPPER_BULB else WEATHERED_COPPER_BULB
            OxidizationLevel.OXIDIZED -> if (waxed) WAXED_OXIDIZED_COPPER_BULB else OXIDIZED_COPPER_BULB
        }
    }
}
