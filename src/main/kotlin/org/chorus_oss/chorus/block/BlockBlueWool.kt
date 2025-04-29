package org.chorus_oss.chorus.block

import org.chorus_oss.chorus.tags.BlockTags
import org.chorus_oss.chorus.utils.DyeColor

class BlockBlueWool @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockWool(blockstate) {
    override fun getDyeColor(): DyeColor {
        return DyeColor.BLUE
    }

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.BLUE_WOOL, setOf(BlockTags.PNX_WOOL))

    }
}