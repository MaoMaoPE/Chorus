package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockPolishedBlackstoneButton @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockStoneButton(blockstate) {
    override val name: String
        get() = "Polished Blackstone Button"

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(
            BlockID.POLISHED_BLACKSTONE_BUTTON,
            CommonBlockProperties.BUTTON_PRESSED_BIT,
            CommonBlockProperties.FACING_DIRECTION
        )
    }
}