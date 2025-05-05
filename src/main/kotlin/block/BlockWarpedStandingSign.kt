package org.chorus_oss.chorus.block

import org.chorus_oss.chorus.block.property.CommonBlockProperties
import org.chorus_oss.chorus.item.Item
import org.chorus_oss.chorus.item.ItemWarpedSign

class BlockWarpedStandingSign @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockStandingSign(blockstate) {
    override fun getWallSignId() = BlockWarpedWallSign.properties.identifier

    override fun toItem(): Item {
        return ItemWarpedSign()
    }

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.WARPED_STANDING_SIGN, CommonBlockProperties.GROUND_SIGN_DIRECTION)
    }
}