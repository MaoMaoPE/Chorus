package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockCherrySlab @JvmOverloads constructor(blockState: BlockState = Companion.properties.defaultState) :
    BlockWoodenSlab(blockState, BlockID.CHERRY_DOUBLE_SLAB) {
    override fun getSlabName(): String {
        return "Cherry"
    }

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.CHERRY_SLAB, CommonBlockProperties.MINECRAFT_VERTICAL_HALF)
    }
}