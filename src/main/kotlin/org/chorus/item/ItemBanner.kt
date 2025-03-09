package org.chorus.item

import org.chorus.block.BlockStandingBanner
import org.chorus.block.property.CommonBlockProperties
import org.chorus.nbt.tag.CompoundTag
import org.chorus.network.protocol.types.BannerPattern
import org.chorus.utils.DyeColor
import java.util.*

/**
 * @author PetteriM1
 */
class ItemBanner @JvmOverloads constructor(meta: Int = 0, count: Int = 1) :
    Item(ItemID.Companion.BANNER, meta, count, "Banner") {
    override fun internalAdjust() {
        block = BlockStandingBanner.properties.getBlockState(
            CommonBlockProperties.GROUND_SIGN_DIRECTION.createValue(
                damage
            )
        ).toBlock()
        name = baseDyeColor.getName() + " Banner"
    }

    override val maxStackSize: Int
        get() = 16

    val baseColor: Int
        get() = this.damage and 0x0f

    fun setBaseColor(color: DyeColor) {
        this.damage = color.dyeData and 0x0f
    }

    val baseDyeColor: DyeColor
        get() = Objects.requireNonNull(DyeColor.getByDyeData(baseColor))

    var type: Int
        get() = getOrCreateNamedTag().getInt("Type")
        set(type) {
            val tag =
                checkNotNull(if (this.hasCompoundTag()) this.namedTag else CompoundTag())
            tag.putInt("Type", type)
            this.setNamedTag(tag)
        }

    fun addPattern(bannerPattern: BannerPattern) {
        val tag = checkNotNull(if (this.hasCompoundTag()) this.namedTag else CompoundTag())
        val patterns = tag.getList("Patterns", CompoundTag::class.java)
        patterns.add(
            CompoundTag().putInt
                ("Color", bannerPattern.color.dyeData and 0x0f).putString
                ("Pattern", bannerPattern.type.code)
        )
        tag.putList("Patterns", patterns)
        this.setNamedTag(tag)
    }

    fun getPattern(index: Int): BannerPattern {
        val tag = checkNotNull(if (this.hasCompoundTag()) this.namedTag else CompoundTag())
        return BannerPattern.fromCompoundTag(
            if (tag.getList("Patterns").size() > index && index >= 0) tag.getList(
                "Patterns",
                CompoundTag::class.java
            )[index] else CompoundTag()
        )
    }

    fun removePattern(index: Int) {
        val tag = checkNotNull(if (this.hasCompoundTag()) this.namedTag else CompoundTag())
        val patterns = tag.getList("Patterns", CompoundTag::class.java)
        if (patterns.size() > index && index >= 0) {
            patterns.remove(index)
        }
        this.setNamedTag(tag)
    }

    val patternsSize: Int
        get() = (if (this.hasCompoundTag()) this.namedTag else CompoundTag()).getList(
            "Patterns"
        ).size()

    fun hasPattern(): Boolean {
        return (if (this.hasCompoundTag()) this.namedTag else CompoundTag()).contains("Patterns")
    }
}
