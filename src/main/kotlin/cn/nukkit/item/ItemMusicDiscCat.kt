package cn.nukkit.item

class ItemMusicDiscCat : ItemMusicDisc(ItemID.Companion.MUSIC_DISC_CAT) {
    override val soundId: String
        get() = "record.cat"
}