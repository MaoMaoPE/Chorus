package org.chorus.block

class BlockBlackTerracotta @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockHardenedClay(blockstate) {

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.BLACK_TERRACOTTA)
    }
}