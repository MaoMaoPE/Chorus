package org.chorus.block

class BlockPinkConcretePowder @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockConcretePowder(blockstate) {
    override fun getConcrete() = BlockPinkConcrete()

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.PINK_CONCRETE_POWDER)
    }
}