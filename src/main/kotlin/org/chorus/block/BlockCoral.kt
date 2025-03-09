package org.chorus.block

import org.chorus.Player
import org.chorus.block.property.CommonBlockProperties
import org.chorus.block.property.type.IntPropertyType
import org.chorus.event.Event.isCancelled
import org.chorus.item.*
import org.chorus.item.enchantment.Enchantment
import org.chorus.level.Level
import org.chorus.math.BlockFace
import java.util.concurrent.ThreadLocalRandom

abstract class BlockCoral(blockstate: BlockState?) : BlockFlowable(blockstate) {
    abstract val isDead: Boolean

    abstract val deadCoral: Block?

    fun setDead(deadBlock: Block) {
        level.setBlock(this.position, deadBlock, true, true)
    }

    override val waterloggingLevel: Int
        get() = 2

    override fun onUpdate(type: Int): Int {
        if (type == Level.BLOCK_UPDATE_NORMAL) {
            val down = down()
            if (!down!!.isSolid) {
                level.useBreakOn(this.position)
            } else if (!isDead) {
                level.scheduleUpdate(this, 60 + ThreadLocalRandom.current().nextInt(40))
            }
            return type
        } else if (type == Level.BLOCK_UPDATE_SCHEDULED) {
            if (!isDead && (getLevelBlockAtLayer(1) !is BlockFlowingWater) && (getLevelBlockAtLayer(1) !is BlockFrostedIce)) {
                val event: BlockFadeEvent = BlockFadeEvent(this, deadCoral)
                if (!event.isCancelled) {
                    setDead(event.newState)
                }
            }
            return type
        }
        return 0
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
        val down = down()
        val layer1 = block.getLevelBlockAtLayer(1)
        val hasWater = layer1 is BlockFlowingWater
        val waterDamage: Int
        if (!layer1!!.isAir && (!hasWater || ((layer1.getPropertyValue<Int, IntPropertyType>(CommonBlockProperties.LIQUID_DEPTH)
                .also { waterDamage = it }) != 0) && waterDamage != 8)
        ) {
            return false
        }

        if (hasWater && layer1.getPropertyValue<Int, IntPropertyType>(CommonBlockProperties.LIQUID_DEPTH) == 8) {
            level.setBlock(this.position, 1, BlockFlowingWater(), true, false)
        }

        if (down!!.isSolid) {
            level.setBlock(this.position, 0, this, true, true)
            return true
        }
        return false
    }

    override fun canSilkTouch(): Boolean {
        return true
    }

    override fun getDrops(item: Item): Array<Item?>? {
        return if (item.getEnchantment(Enchantment.ID_SILK_TOUCH) != null) {
            super.getDrops(item)
        } else {
            Item.EMPTY_ARRAY
        }
    }

    companion object {
        const val TYPE_TUBE: Int = 0
        const val TYPE_BRAIN: Int = 1
        const val TYPE_BUBBLE: Int = 2
        const val TYPE_FIRE: Int = 3
        const val TYPE_HORN: Int = 4
    }
}
