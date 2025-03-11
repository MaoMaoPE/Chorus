package org.chorus.block

class BlockBlueConcretePowder @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockConcretePowder(blockstate) {
    override fun getConcrete(): BlockConcrete {
        return BlockBlueConcrete()
    }

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.BLUE_CONCRETE_POWDER)
    }
}