package org.chorus_oss.chorus.block

import org.chorus_oss.chorus.item.Item
import org.chorus_oss.chorus.item.ItemShulkerBox
import org.chorus_oss.chorus.tags.BlockTags

class BlockRedShulkerBox @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockUndyedShulkerBox(blockstate) {
    override fun getShulkerBox(): Item {
        return ItemShulkerBox(14)
    }

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.RED_SHULKER_BOX, setOf(BlockTags.PNX_SHULKERBOX))
    }
}