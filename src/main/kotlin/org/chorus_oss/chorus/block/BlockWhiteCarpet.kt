package org.chorus_oss.chorus.block

class BlockWhiteCarpet @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockCarpet(blockstate) {

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.WHITE_CARPET)
    }
}