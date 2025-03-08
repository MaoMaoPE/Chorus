package cn.nukkit.item

import cn.nukkit.entity.effect.PotionType
import cn.nukkit.nbt.tag.CompoundTag

class ItemLingeringPotion @JvmOverloads constructor(meta: Int = 0, count: Int = 1) :
    ProjectileItem(ItemID.Companion.LINGERING_POTION, meta, count, "Lingering Potion") {
    init {
        updateName()
    }

    override var damage: Int
        get() = super.damage
        set(meta) {
            super.setDamage(meta)
            updateName()
        }

    private fun updateName() {
        val potion = PotionType.get(getDamage())
        name = if (PotionType.WATER.equals(potion)) {
            "Lingering Water Bottle"
        } else {
            ItemPotion.Companion.buildName(potion, "Lingering Potion", true)
        }
    }

    override val maxStackSize: Int
        get() = 1

    override fun canBeActivated(): Boolean {
        return true
    }

    override val projectileEntityType: String
        get() = ItemID.Companion.LINGERING_POTION

    override val throwForce: Float
        get() = 0.5f

    override fun correctNBT(nbt: CompoundTag) {
        nbt.putInt("PotionId", this.meta)
    }
}