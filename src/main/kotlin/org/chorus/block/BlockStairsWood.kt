package org.chorus.block

import org.chorus.item.Item
import org.chorus.item.ItemTool

/**
 * @author xtypr
 * @since 2015/11/25
 */
abstract class BlockStairsWood(blockState: BlockState?) : BlockStairs(blockState) {
    override val toolType: Int
        get() = ItemTool.TYPE_AXE

    override val hardness: Double
        get() = 2.0

    override val resistance: Double
        get() = 3.0

    override val burnChance: Int
        get() = 5

    override val burnAbility: Int
        get() = 20

    override fun getDrops(item: Item): Array<Item?>? {
        return arrayOf(
            toItem()
        )
    }
}
