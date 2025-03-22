package org.chorus.block

import org.chorus.block.property.CommonBlockProperties
import org.chorus.item.Item

class BlockMushroomStem @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockMushroomBlock(blockstate) {
    override fun getDrops(item: Item): Array<Item> {
        return Item.EMPTY_ARRAY
    }

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.MUSHROOM_STEM, CommonBlockProperties.HUGE_MUSHROOM_BITS)
    }
}