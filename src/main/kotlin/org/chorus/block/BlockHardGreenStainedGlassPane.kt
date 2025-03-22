package org.chorus.block

class BlockHardGreenStainedGlassPane(blockstate: BlockState) : Block(blockstate) {

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.HARD_GREEN_STAINED_GLASS_PANE)
    }
}