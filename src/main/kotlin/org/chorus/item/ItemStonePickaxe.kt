package org.chorus.item

class ItemStonePickaxe @JvmOverloads constructor(meta: Int = 0, count: Int = 1) :
    ItemTool(ItemID.Companion.STONE_PICKAXE, meta, count, "Stone Pickaxe") {
    override val maxDurability: Int
        get() = DURABILITY_STONE

    override val isPickaxe: Boolean
        get() = true

    override val tier: Int
        get() = TIER_STONE

    override val attackDamage: Int
        get() = 3
}