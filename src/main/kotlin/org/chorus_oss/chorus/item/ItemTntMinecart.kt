package org.chorus_oss.chorus.item

import org.chorus_oss.chorus.Player
import org.chorus_oss.chorus.block.*
import org.chorus_oss.chorus.entity.*
import org.chorus_oss.chorus.entity.Entity.Companion.createEntity
import org.chorus_oss.chorus.entity.item.EntityTntMinecart
import org.chorus_oss.chorus.level.Level
import org.chorus_oss.chorus.math.BlockFace
import org.chorus_oss.chorus.nbt.tag.*
import org.chorus_oss.chorus.utils.Rail

class ItemTntMinecart @JvmOverloads constructor(meta: Int = 0, count: Int = 1) :
    Item(ItemID.Companion.TNT_MINECART, meta, count, "Minecart with TNT") {
    override fun canBeActivated(): Boolean {
        return true
    }

    override fun onActivate(
        level: Level,
        player: Player,
        block: Block,
        target: Block,
        face: BlockFace,
        fx: Double,
        fy: Double,
        fz: Double
    ): Boolean {
        if (Rail.isRailBlock(target)) {
            val type = (target as BlockRail).getOrientation()
            var adjacent = 0.0
            if (type != null) {
                if (type.isAscending) {
                    adjacent = 0.5
                }
            }
            val minecart = createEntity(
                EntityID.TNT_MINECART,
                level.getChunk(target.position.floorX shr 4, target.position.floorZ shr 4), CompoundTag()
                    .putList(
                        "Pos", ListTag<FloatTag>()
                            .add(FloatTag(target.x + 0.5))
                            .add(FloatTag(target.y + 0.0625 + adjacent))
                            .add(FloatTag(target.z + 0.5))
                    )
                    .putList(
                        "Motion", ListTag<FloatTag>()
                            .add(FloatTag(0f))
                            .add(FloatTag(0f))
                            .add(FloatTag(0f))
                    )
                    .putList(
                        "Rotation", ListTag<FloatTag>()
                            .add(FloatTag(0f))
                            .add(FloatTag(0f))
                    )
            ) as EntityTntMinecart?

            if (minecart == null) {
                return false
            }

            if (player.isAdventure || player.isSurvival) {
                val item = player.inventory.itemInHand
                item.setCount(item.getCount() - 1)
                player.inventory.setItemInHand(item)
            }

            minecart.spawnToAll()
            return true
        }
        return false
    }

    override val maxStackSize: Int
        get() = 1
}