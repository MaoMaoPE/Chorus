package cn.nukkit.item

class ItemNetheriteHoe @JvmOverloads constructor(meta: Int = 0, count: Int = 1) :
    ItemTool(ItemID.Companion.NETHERITE_HOE, meta, count, "Netherite Hoe") {
    override val isHoe: Boolean
        get() = true

    override val attackDamage: Int
        get() = 6

    override val tier: Int
        get() = ItemTool.Companion.TIER_NETHERITE

    override val maxDurability: Int
        get() = ItemTool.Companion.DURABILITY_NETHERITE

    override val isLavaResistant: Boolean
        get() = true
}