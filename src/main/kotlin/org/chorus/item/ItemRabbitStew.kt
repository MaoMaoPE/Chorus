package org.chorus.item

import org.chorus.Player

class ItemRabbitStew @JvmOverloads constructor(meta: Int = 0, count: Int = 1) :
    ItemFood(ItemID.Companion.RABBIT_STEW, meta, count, "Rabbit Stew") {
    override val maxStackSize: Int
        get() = 1

    override val foodRestore: Int
        get() = 10

    override val saturationRestore: Float
        get() = 12f

    override fun onEaten(player: Player): Boolean {
        player.getInventory().addItem(ItemBowl())

        return true
    }
}
