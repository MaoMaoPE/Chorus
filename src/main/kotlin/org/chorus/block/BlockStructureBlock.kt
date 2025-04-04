package org.chorus.block

import org.chorus.Player
import org.chorus.block.property.CommonBlockProperties
import org.chorus.block.property.enums.StructureBlockType
import org.chorus.blockentity.BlockEntity
import org.chorus.blockentity.BlockEntityID
import org.chorus.blockentity.BlockEntityStructBlock
import org.chorus.item.Item
import org.chorus.math.BlockFace
import org.chorus.math.Vector3

class BlockStructureBlock @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockSolid(blockstate), BlockEntityHolder<BlockEntityStructBlock> {
    var structureBlockType: StructureBlockType
        get() = getPropertyValue(CommonBlockProperties.STRUCTURE_BLOCK_TYPE)
        set(type) {
            setPropertyValue(
                CommonBlockProperties.STRUCTURE_BLOCK_TYPE,
                type
            )
        }

    override fun canBeActivated(): Boolean {
        return true
    }

    override fun onActivate(
        item: Item,
        player: Player?,
        blockFace: BlockFace,
        fx: Float,
        fy: Float,
        fz: Float
    ): Boolean {
        if (player != null) {
            if (player.isCreative && player.isOp) {
                val tile = this.getOrCreateBlockEntity()
                tile.spawnTo(player)
                player.addWindow(tile.inventory)
            }
        }
        return true
    }

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
        if (player != null && (!player.isCreative || !player.isOp)) {
            return false
        }
        return BlockEntityHolder.setBlockAndCreateEntity(this, true, true) != null
    }

    override val name: String
        get() = structureBlockType.name + "Structure Block"

    override val hardness: Double
        get() = -1.0

    override val resistance: Double
        get() = 18000000.0

    override fun canHarvestWithHand(): Boolean {
        return false
    }

    override fun isBreakable(vector: Vector3, layer: Int, face: BlockFace?, item: Item?, player: Player?): Boolean {
        return player != null && player.isCreative
    }

    override fun canBePushed(): Boolean {
        return false
    }

    override fun canBePulled(): Boolean {
        return false
    }

    override fun getBlockEntityClass() = BlockEntityStructBlock::class.java

    override fun getBlockEntityType() = BlockEntityID.STRUCTURE_BLOCK

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.STRUCTURE_BLOCK, CommonBlockProperties.STRUCTURE_BLOCK_TYPE)
    }
}