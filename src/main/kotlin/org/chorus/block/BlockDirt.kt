package org.chorus.block

import org.chorus.Player
import org.chorus.item.*
import org.chorus.level.Sound
import org.chorus.math.BlockFace

open class BlockDirt : BlockSolid, Natural {
    constructor() : super(Companion.properties.defaultState)

    constructor(blockState: BlockState?) : super(blockState)

    override fun canBeActivated(): Boolean {
        return true
    }

    override val resistance: Double
        get() = 0.5

    override val hardness: Double
        get() =//Although the hardness on the wiki is 0.5, after testing, a hardness of 0.6 is more suitable for the vanilla
            0.6

    override val toolType: Int
        get() = ItemTool.TYPE_SHOVEL

    override fun onActivate(
        item: Item,
        player: Player?,
        blockFace: BlockFace?,
        fx: Float,
        fy: Float,
        fz: Float
    ): Boolean {
        if (!up()!!.canBeReplaced()) {
            return false
        }

        if (item.isHoe) {
            item.useOn(this)
            level.setBlock(this.position, get(FARMLAND), true)
            if (player != null) {
                player.level!!.addSound(player.position, Sound.USE_GRASS)
            }
            return true
        } else if (item.isShovel) {
            item.useOn(this)
            level.setBlock(this.position, get(GRASS_PATH))
            if (player != null) {
                player.level!!.addSound(player.position, Sound.USE_GRASS)
            }
            return true
        }

        return false
    }

    override fun getDrops(item: Item): Array<Item?>? {
        return arrayOf(this.toItem())
    }

    override fun toItem(): Item? {
        return ItemBlock(this)
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.DIRT)

    }
}
