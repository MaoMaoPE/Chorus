package org.chorus.block

import org.chorus.Player
import org.chorus.block.property.CommonBlockProperties
import org.chorus.item.Item
import org.chorus.math.BlockFace

class BlockQuartzPillar @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockQuartzBlock(blockstate) {
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
        this.pillarAxis = face.axis
        level.setBlock(block.position, this, true, true)
        return true
    }

    var pillarAxis: BlockFace.Axis?
        get() = getPropertyValue(
            CommonBlockProperties.PILLAR_AXIS
        )
        set(axis) {
            setPropertyValue(
                CommonBlockProperties.PILLAR_AXIS,
                axis
            )
        }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.QUARTZ_PILLAR, CommonBlockProperties.PILLAR_AXIS)

    }
}
