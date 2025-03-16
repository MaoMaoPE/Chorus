package org.chorus.block

import org.chorus.block.property.CommonBlockProperties
import org.chorus.item.Item
import org.chorus.item.ItemCreeperHead

class BlockCreeperHead(blockState: BlockState) : BlockHead(blockState) {
    override val name: String
        get() = "Creeper Head"

    override fun getDrops(item: Item): Array<Item> {
        return arrayOf(
            this.toItem()
        )
    }

    override fun toItem(): Item {
        return ItemCreeperHead()
    }

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.CREEPER_HEAD, CommonBlockProperties.FACING_DIRECTION)
    }
}
