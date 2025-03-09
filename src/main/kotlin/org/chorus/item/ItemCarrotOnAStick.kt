package org.chorus.item

/**
 * @author lion
 * @since 21.03.17
 */
class ItemCarrotOnAStick @JvmOverloads constructor(meta: Int = 0, count: Int = 1) :
    ItemTool(ItemID.Companion.CARROT_ON_A_STICK, meta, count, "Carrot on a Stick") {
    override val maxDurability: Int
        get() = ItemTool.Companion.DURABILITY_CARROT_ON_A_STICK

    override val maxStackSize: Int
        get() = 1

    override fun noDamageOnAttack(): Boolean {
        return true
    }

    override fun noDamageOnBreak(): Boolean {
        return true
    }
}

