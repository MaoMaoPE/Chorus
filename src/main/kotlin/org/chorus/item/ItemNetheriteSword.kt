package org.chorus.item

class ItemNetheriteSword @JvmOverloads constructor(meta: Int = 0, count: Int = 1) :
    ItemTool(ItemID.Companion.NETHERITE_SWORD, meta, count, "Netherite Sword") {
    override val maxDurability: Int
        get() = ItemTool.Companion.DURABILITY_NETHERITE

    override val isSword: Boolean
        get() = true

    override val tier: Int
        get() = ItemTool.Companion.TIER_NETHERITE

    override val attackDamage: Int
        get() = 8

    override val isLavaResistant: Boolean
        get() = true
}