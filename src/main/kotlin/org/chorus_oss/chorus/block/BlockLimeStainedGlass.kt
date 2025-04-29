package org.chorus_oss.chorus.block

import org.chorus_oss.chorus.utils.DyeColor

class BlockLimeStainedGlass @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockGlassStained(blockstate) {
    override fun getDyeColor() = DyeColor.LIME

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.LIME_STAINED_GLASS)

    }
}