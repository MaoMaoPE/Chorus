package org.chorus.block

import org.chorus.block.property.CommonBlockProperties
import org.chorus.level.Sound

class BlockWarpedTrapdoor @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockTrapdoor(blockstate) {
    override val name: String
        get() = "Warped Trapdoor"

    override val burnChance: Int
        get() = 0

    override val burnAbility: Int
        get() = 0

    override fun playOpenSound() {
        level.addSound(this.position, Sound.OPEN_NETHER_WOOD_TRAPDOOR)
    }

    override fun playCloseSound() {
        level.addSound(this.position, Sound.CLOSE_NETHER_WOOD_TRAPDOOR)
    }

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(
            BlockID.WARPED_TRAPDOOR,
            CommonBlockProperties.DIRECTION,
            CommonBlockProperties.OPEN_BIT,
            CommonBlockProperties.UPSIDE_DOWN_BIT
        )

    }
}