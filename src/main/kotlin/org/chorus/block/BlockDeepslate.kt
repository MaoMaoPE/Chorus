package org.chorus.block

import org.chorus.block.property.CommonBlockProperties
import org.chorus.item.Item
import org.chorus.item.ItemTool

class BlockDeepslate : BlockSolid {
    constructor() : super(Companion.properties.defaultState)

    constructor(blockState: BlockState) : super(blockState)

    override val name: String
        get() = "Deepslate"

    override val hardness: Double
        get() = 3.0

    override val resistance: Double
        get() = 6.0

    override val toolType: Int
        get() = ItemTool.TYPE_PICKAXE

    override val toolTier: Int
        get() = ItemTool.TIER_WOODEN

    override fun canHarvestWithHand(): Boolean {
        return false
    }

    override fun getDrops(item: Item): Array<Item> {
        if (!canHarvest(item)) {
            return Item.EMPTY_ARRAY
        }

        return arrayOf(Item.get(BlockID.COBBLED_DEEPSLATE))
    }

    override fun canSilkTouch(): Boolean {
        return true
    }

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.DEEPSLATE, CommonBlockProperties.PILLAR_AXIS)
    }
}
