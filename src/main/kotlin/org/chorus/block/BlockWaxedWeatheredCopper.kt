package org.chorus.block

class BlockWaxedWeatheredCopper @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockWeatheredCopper(blockstate) {
    override val name: String
        get() = "Waxed Weathered Copper"

    override val isWaxed: Boolean
        get() = true

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.WAXED_WEATHERED_COPPER)
    }
}