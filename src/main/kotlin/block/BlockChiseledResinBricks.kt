package org.chorus_oss.chorus.block

class BlockChiseledResinBricks @JvmOverloads constructor(blockstate: BlockState = properties.defaultState) :
    BlockResinBricks(blockstate) {

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.CHISELED_RESIN_BRICKS)
    }
}
