package org.chorus.item

class ItemLavaBucket : ItemBucket(ItemID.Companion.LAVA_BUCKET) {
    override var damage: Int
        get() = super.damage
        set(meta) {
        }
}