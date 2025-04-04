package org.chorus.block

class BlockSpruceFence @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockFence(blockstate) {
    override val name: String
        get() = "Spruce Fence"

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.SPRUCE_FENCE)
    }
}