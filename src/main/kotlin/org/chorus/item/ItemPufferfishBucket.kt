package org.chorus.item

class ItemPufferfishBucket : ItemBucket(ItemID.Companion.PUFFERFISH_BUCKET) {
    override var damage: Int
        get() = super.damage
        set(meta) {
        }
}