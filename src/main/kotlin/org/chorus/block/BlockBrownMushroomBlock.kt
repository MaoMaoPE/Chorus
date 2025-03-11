package org.chorus.block

import org.chorus.block.property.CommonBlockProperties
import org.chorus.item.Item
import java.util.concurrent.ThreadLocalRandom

class BlockBrownMushroomBlock @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockMushroomBlock(blockstate) {
    override fun getDrops(item: Item): Array<Item?> {
        return if (ThreadLocalRandom.current().nextInt(1, 20) == 1) {
            arrayOf(
                Item.get(BlockID.BROWN_MUSHROOM_BLOCK)
            )
        } else {
            Item.EMPTY_ARRAY
        }
    }

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.BROWN_MUSHROOM_BLOCK, CommonBlockProperties.HUGE_MUSHROOM_BITS)
    }
}