package org.chorus_oss.chorus.block

class BlockHardGlassPane @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    Block(blockstate) {

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.HARD_GLASS_PANE)
    }
}