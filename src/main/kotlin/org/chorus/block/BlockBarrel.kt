package org.chorus.block

import cn.nukkit.Player
import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.block.property.type.BooleanPropertyType
import cn.nukkit.block.property.type.IntPropertyType
import cn.nukkit.blockentity.*
import cn.nukkit.inventory.ContainerInventory.Companion.calculateRedstone
import cn.nukkit.item.*
import cn.nukkit.math.BlockFace
import cn.nukkit.math.BlockFace.Companion.fromIndex
import cn.nukkit.nbt.tag.CompoundTag
import cn.nukkit.nbt.tag.ListTag
import cn.nukkit.nbt.tag.StringTag
import cn.nukkit.nbt.tag.Tag
import cn.nukkit.utils.Faceable
import kotlin.math.abs

class BlockBarrel @JvmOverloads constructor(blockState: BlockState? = Companion.properties.defaultState) :
    BlockSolid(blockState), Faceable, BlockEntityHolder<BlockEntityBarrel?> {
    override val name: String
        get() = "Barrel"

    override fun getBlockEntityType(): String {
        return BlockEntity.BARREL
    }

    override fun getBlockEntityClass(): Class<out BlockEntityBarrel> {
        return BlockEntityBarrel::class.java
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
        if (player == null) {
            blockFace = BlockFace.UP
        } else {
            if (abs(player.position.x - position.x) < 2 && abs(player.position.z - position.z) < 2) {
                val y = player.position.y + player.getEyeHeight()

                blockFace = if (y - position.y > 2) {
                    BlockFace.UP
                } else if (position.y - y > 0) {
                    BlockFace.DOWN
                } else {
                    player.getHorizontalFacing().getOpposite()
                }
            } else {
                blockFace = player.getHorizontalFacing().getOpposite()
            }
        }

        val nbt = CompoundTag().putList("Items", ListTag())

        if (item.hasCustomName()) {
            nbt!!.putString("CustomName", item.customName)
        }

        if (item.hasCustomBlockData()) {
            val customData: Map<String?, Tag?> = item.customBlockData!!.getTags()
            for ((key, value) in customData) {
                nbt!!.put(key, value)
            }
        }

        return BlockEntityHolder.setBlockAndCreateEntity(this, true, true, nbt) != null
    }

    override fun onActivate(
        item: Item,
        player: Player,
        blockFace: BlockFace?,
        fx: Float,
        fy: Float,
        fz: Float
    ): Boolean {
        if (isNotActivate(player)) return false

        val barrel = getOrCreateBlockEntity()

        if (barrel.namedTag.contains("Lock") && barrel.namedTag.get("Lock") is StringTag
            && (barrel.namedTag.getString("Lock") != item.customName)
        ) {
            return false
        }

        player.addWindow(barrel.getInventory())
        return true
    }

    override fun canBeActivated(): Boolean {
        return true
    }

    override val hardness: Double
        get() = 2.5

    override val resistance: Double
        get() = 12.5

    override val toolType: Int
        get() = ItemTool.TYPE_AXE

    override fun toItem(): Item? {
        return ItemBlock(BlockBarrel())
    }

    override var blockFace: BlockFace?
        get() = fromIndex(getPropertyValue<Int, IntPropertyType>(CommonBlockProperties.FACING_DIRECTION))
        set(face) {
            setPropertyValue<Int, IntPropertyType>(CommonBlockProperties.FACING_DIRECTION, face!!.index)
        }

    var isOpen: Boolean
        get() = getPropertyValue<Boolean, BooleanPropertyType>(CommonBlockProperties.OPEN_BIT)
        set(open) {
            setPropertyValue<Boolean, BooleanPropertyType>(CommonBlockProperties.OPEN_BIT, open)
        }

    override fun hasComparatorInputOverride(): Boolean {
        return true
    }

    override val comparatorInputOverride: Int
        get() {
            val blockEntity = blockEntity

            if (blockEntity != null) {
                return calculateRedstone(blockEntity.getInventory())
            }

            return super.comparatorInputOverride
        }

    companion object {
        val properties: BlockProperties =
            BlockProperties(BARREL, CommonBlockProperties.FACING_DIRECTION, CommonBlockProperties.OPEN_BIT)
            get() = Companion.field
    }
}
