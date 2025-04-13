package org.chorus.block

import org.chorus.Player
import org.chorus.block.property.CommonBlockProperties
import org.chorus.item.Item
import org.chorus.math.BlockFace

class BlockLava @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockFlowingLava(blockstate) {
    override val name: String
        get() = "Still Lava"

    override fun getLiquidWithNewDepth(depth: Int): BlockLiquid {
        return BlockLava(
            blockState.setPropertyValue(
                Companion.properties,
                CommonBlockProperties.LIQUID_DEPTH.createValue(depth)
            )
        )
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
        return level.setBlock(this.position, this, true, false)
    }

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.LAVA, CommonBlockProperties.LIQUID_DEPTH)
    }
}
