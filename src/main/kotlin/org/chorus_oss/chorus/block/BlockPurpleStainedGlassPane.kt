package org.chorus_oss.chorus.block

import org.chorus_oss.chorus.utils.DyeColor

class BlockPurpleStainedGlassPane @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockGlassPaneStained(blockstate) {
    override fun getDyeColor() = DyeColor.PURPLE

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.PURPLE_STAINED_GLASS_PANE)

    }
}