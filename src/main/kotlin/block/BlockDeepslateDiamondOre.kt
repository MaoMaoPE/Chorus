package org.chorus_oss.chorus.block

class BlockDeepslateDiamondOre @JvmOverloads constructor(blockstate: BlockState = properties.defaultState) :
    BlockDiamondOre(blockstate) {
    override val name: String
        get() = "Deepslate Diamond Ore"

    override val hardness: Double
        get() = 4.5

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.DEEPSLATE_DIAMOND_ORE)
    }
}