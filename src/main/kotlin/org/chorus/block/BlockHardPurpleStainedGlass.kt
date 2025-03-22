package org.chorus.block

class BlockHardPurpleStainedGlass(blockstate: BlockState) : Block(blockstate) {

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.HARD_PURPLE_STAINED_GLASS)
    }
}