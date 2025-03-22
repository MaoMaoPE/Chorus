package org.chorus.block

import org.chorus.block.property.CommonBlockProperties
import org.chorus.block.property.enums.WoodType
import org.chorus.item.Item

class BlockJungleLeaves(blockstate: BlockState) : BlockLeaves(blockstate) {
    override fun getType(): WoodType {
        return WoodType.JUNGLE
    }

    override fun toSapling(): Item {
        return Item.get(BlockID.JUNGLE_SAPLING)
    }

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(
            BlockID.JUNGLE_LEAVES,
            CommonBlockProperties.PERSISTENT_BIT,
            CommonBlockProperties.UPDATE_BIT
        )
    }
}