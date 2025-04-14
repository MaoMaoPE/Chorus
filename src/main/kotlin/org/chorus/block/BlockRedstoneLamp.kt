package org.chorus.block

import org.chorus.Player
import org.chorus.Server
import org.chorus.event.redstone.RedstoneUpdateEvent
import org.chorus.item.Item
import org.chorus.item.ItemBlock
import org.chorus.item.ItemTool
import org.chorus.level.Level
import org.chorus.math.BlockFace
import org.chorus.utils.RedstoneComponent

open class BlockRedstoneLamp @JvmOverloads constructor(blockState: BlockState = Companion.properties.defaultState) :
    BlockSolid(blockState), RedstoneComponent {
    override val name: String
        get() = "Redstone Lamp"

    override val hardness: Double
        get() = 0.3

    override val resistance: Double
        get() = 1.5

    override val toolType: Int
        get() = ItemTool.TYPE_PICKAXE

    override fun place(
        item: Item?,
        block: Block,
        target: Block?,
        face: BlockFace,
        fx: Double,
        fy: Double,
        fz: Double,
        player: Player?
    ): Boolean {
        if (this.isGettingPower) {
            level.setBlock(this.position, get(BlockID.LIT_REDSTONE_LAMP), false, true)
        } else {
            level.setBlock(this.position, this, false, true)
        }
        return true
    }

    override fun onUpdate(type: Int): Int {
        if (type == Level.BLOCK_UPDATE_NORMAL || type == Level.BLOCK_UPDATE_REDSTONE) {
            if (!Server.instance.settings.levelSettings.enableRedstone) {
                return 0
            }

            if (this.isGettingPower) {
                // Redstone event
                val ev = RedstoneUpdateEvent(this)
                Server.instance.pluginManager.callEvent(ev)
                if (ev.isCancelled) {
                    return 0
                }

                level.updateComparatorOutputLevelSelective(this.position, true)

                level.setBlock(this.position, get(BlockID.LIT_REDSTONE_LAMP), false, false)
                return 1
            }
        }

        return 0
    }

    override fun getDrops(item: Item): Array<Item> {
        return arrayOf(
            super.toItem()
        )
    }

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.REDSTONE_LAMP)
    }
}
