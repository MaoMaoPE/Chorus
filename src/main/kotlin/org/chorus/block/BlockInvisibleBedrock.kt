package org.chorus.block

import org.chorus.Player
import org.chorus.item.Item
import org.chorus.item.ItemBlock
import org.chorus.math.BlockFace
import org.chorus.math.Vector3

class BlockInvisibleBedrock @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockSolid(blockstate) {
    override val name: String
        get() = "Invisible Bedrock"

    override val waterloggingLevel: Int
        get() = 2

    override fun canBeFlowedInto(): Boolean {
        return false
    }

    override val hardness: Double
        get() = -1.0

    override val resistance: Double
        get() = 18000000.0

    override fun isBreakable(vector: Vector3, layer: Int, face: BlockFace?, item: Item?, player: Player?): Boolean {
        return false
    }

    override fun canBePushed(): Boolean {
        return false
    }

    override fun canBePulled(): Boolean {
        return false
    }

    override fun toItem(): Item {
        return ItemBlock(get(BlockID.AIR),)
    }

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.INVISIBLE_BEDROCK)
    }
}