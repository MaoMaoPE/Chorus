package org.chorus.item

class ItemMusicDiscMellohi : ItemMusicDisc(ItemID.Companion.MUSIC_DISC_MELLOHI) {
    override val soundId: String
        get() = "record.mellohi"
}