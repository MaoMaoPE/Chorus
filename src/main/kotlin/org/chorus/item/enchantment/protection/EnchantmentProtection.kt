package org.chorus.item.enchantment.protection

import cn.nukkit.item.Item
import cn.nukkit.item.ItemElytra
import cn.nukkit.item.enchantment.Enchantment
import cn.nukkit.item.enchantment.EnchantmentType

/**
 * @author MagicDroidX (Nukkit Project)
 */
abstract class EnchantmentProtection protected constructor(
    id: Int,
    name: String,
    rarity: Rarity,
    protected val protectionType: TYPE
) :
    Enchantment(id, name, rarity, EnchantmentType.ARMOR) {
    enum class TYPE {
        ALL,
        FIRE,
        FALL,
        EXPLOSION,
        PROJECTILE
    }

    init {
        if (protectionType == TYPE.FALL) {
            this.type = EnchantmentType.ARMOR_FEET
        }
    }

    override fun canEnchant(item: Item): Boolean {
        return item !is ItemElytra && super.canEnchant(item)
    }

    public override fun checkCompatibility(enchantment: Enchantment): Boolean {
        if (enchantment is EnchantmentProtection) {
            if (enchantment.protectionType == this.protectionType) {
                return false
            }
            return enchantment.protectionType == TYPE.FALL || this.protectionType == TYPE.FALL
        }
        return super.checkCompatibility(enchantment)
    }

    override val maxLevel: Int
        get() = 4

    override fun getName(): String {
        return "%enchantment.protect." + this.name
    }

    open val typeModifier: Double
        get() = 0.0

    override val isMajor: Boolean
        get() = true
}
