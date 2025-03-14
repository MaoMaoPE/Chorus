package org.chorus.item

import org.chorus.Player
import org.chorus.entity.effect.EffectType

class ItemChicken : ItemFood {
    constructor() : super(ItemID.Companion.CHICKEN, 0, 1, "Raw Chicken")

    constructor(count: Int) : super(ItemID.Companion.CHICKEN, 0, count, "Raw Chicken")

    override val foodRestore: Int
        get() = 2

    override val saturationRestore: Float
        get() = 1.2f

    override fun onEaten(player: Player): Boolean {
        if (0.3f >= Math.random()) {
            player.addEffect(get(EffectType.HUNGER).setDuration(30 * 20))
        }

        return true
    }
}