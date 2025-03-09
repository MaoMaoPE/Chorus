package cn.nukkit.block

import cn.nukkit.Player
import cn.nukkit.item.Item
import cn.nukkit.level.Level
import cn.nukkit.math.AxisAlignedBB
import cn.nukkit.math.BlockFace

/**
 * @author xtypr
 * @since 2015/12/1
 */
class BlockWaterlily @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockFlowable(blockstate) {
    override val name: String
        get() = "Lily Pad"

    override var minX: Double
        get() = position.x + 0.0625
        set(minX) {
            super.minX = minX
        }

    override var minZ: Double
        get() = position.z + 0.0625
        set(minZ) {
            super.minZ = minZ
        }

    override var maxX: Double
        get() = position.x + 0.9375
        set(maxX) {
            super.maxX = maxX
        }

    override var maxY: Double
        get() = position.y + 0.015625
        set(maxY) {
            super.maxY = maxY
        }

    override var maxZ: Double
        get() = position.z + 0.9375
        set(maxZ) {
            super.maxZ = maxZ
        }

    override fun recalculateBoundingBox(): AxisAlignedBB? {
        return this
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
        if (target is BlockFlowingWater || target.getLevelBlockAtLayer(1) is BlockFlowingWater) {
            val up = target.up()
            if (up!!.isAir) {
                level.setBlock(up.position, this, true, true)
                return true
            }
        }
        return false
    }

    override fun onUpdate(type: Int): Int {
        if (type == Level.BLOCK_UPDATE_NORMAL) {
            val down = this.down()
            if ((down !is BlockFlowingWater) && (down!!.getLevelBlockAtLayer(1) !is BlockFlowingWater) && (down !is BlockFrostedIce) && (down!!.getLevelBlockAtLayer(
                    1
                ) !is BlockFrostedIce)
            ) {
                level.useBreakOn(this.position)
                return Level.BLOCK_UPDATE_NORMAL
            }
        }
        return 0
    }

    override fun toItem(): Item? {
        return ItemBlock(this, 0)
    }

    override fun canPassThrough(): Boolean {
        return false
    }

    override fun canBeFlowedInto(): Boolean {
        return false
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.WATERLILY)
            get() = Companion.field
    }
}
