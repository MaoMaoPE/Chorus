package org.chorus_oss.chorus.block

import org.chorus_oss.chorus.block.property.CommonBlockProperties
import org.chorus_oss.chorus.level.Sound

class BlockWarpedDoor @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockWoodenDoor(blockstate) {
    override val name: String
        get() = "Warped Door Block"

    override fun playOpenSound() {
        level.addSound(this.position, Sound.OPEN_NETHER_WOOD_DOOR)
    }

    override fun playCloseSound() {
        level.addSound(this.position, Sound.CLOSE_NETHER_WOOD_DOOR)
    }

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(
            BlockID.WARPED_DOOR,
            CommonBlockProperties.MINECRAFT_CARDINAL_DIRECTION,
            CommonBlockProperties.OPEN_BIT,
            CommonBlockProperties.UPPER_BLOCK_BIT,
            CommonBlockProperties.DOOR_HINGE_BIT
        )

    }
}