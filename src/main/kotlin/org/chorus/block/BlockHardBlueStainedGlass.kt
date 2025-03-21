package org.chorus.block

class BlockHardBlueStainedGlass(blockstate: BlockState) : Block(blockstate) {

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.HARD_BLUE_STAINED_GLASS)
    }
}