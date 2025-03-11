package org.chorus.item.enchantment

import org.chorus.item.*


class EnchantmentSilkTouch :
    Enchantment(Enchantment.Companion.ID_SILK_TOUCH, "untouching", Rarity.VERY_RARE, EnchantmentType.DIGGER) {
    override fun getMinEnchantAbility(level: Int): Int {
        return 15
    }

    override fun getMaxEnchantAbility(level: Int): Int {
        return super.getMinEnchantAbility(level) + 50
    }

    public override fun checkCompatibility(enchantment: Enchantment): Boolean {
        return super.checkCompatibility(enchantment) && enchantment.id != Enchantment.Companion.ID_FORTUNE_DIGGING
    }

    override fun canEnchant(item: Item): Boolean {
        return item.isShears || super.canEnchant(item)
    }
}
