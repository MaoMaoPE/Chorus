package org.chorus.item

/**
 * @author MagicDroidX (Nukkit Project)
 */
class ItemCookie @JvmOverloads constructor(meta: Int = 0, count: Int = 1) :
    ItemFood(ItemID.Companion.COOKIE, meta, count, "Cookie") {
    override val foodRestore: Int
        get() = 2

    override val saturationRestore: Float
        get() = 0.4f
}
