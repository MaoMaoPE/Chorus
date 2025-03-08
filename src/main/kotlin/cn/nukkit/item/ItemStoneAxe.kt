package cn.nukkit.item

class ItemStoneAxe @JvmOverloads constructor(meta: Int = 0, count: Int = 1) :
    ItemTool(ItemID.Companion.STONE_AXE, meta, count, "Stone Axe") {
    override val maxDurability: Int
        get() = ItemTool.Companion.DURABILITY_STONE

    override val isAxe: Boolean
        get() = true

    override val tier: Int
        get() = ItemTool.Companion.TIER_STONE

    override val attackDamage: Int
        get() = 4

    override fun canBreakShield(): Boolean {
        return true
    }
}