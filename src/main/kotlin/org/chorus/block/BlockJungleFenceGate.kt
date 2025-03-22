package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockJungleFenceGate @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockFenceGate(blockstate) {
    override val name: String
        get() = "Jungle Fence Gate"

    companion object {
        val properties: BlockProperties = BlockProperties(
            BlockID.JUNGLE_FENCE_GATE,
            CommonBlockProperties.IN_WALL_BIT,
            CommonBlockProperties.MINECRAFT_CARDINAL_DIRECTION,
            CommonBlockProperties.OPEN_BIT
        )

    }
}