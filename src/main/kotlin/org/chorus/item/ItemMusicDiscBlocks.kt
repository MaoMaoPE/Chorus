package org.chorus.item

class ItemMusicDiscBlocks : ItemMusicDisc(ItemID.Companion.MUSIC_DISC_BLOCKS) {
    override val soundId: String
        get() = "record.blocks"
}