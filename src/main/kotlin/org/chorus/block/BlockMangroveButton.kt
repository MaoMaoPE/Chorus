package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockMangroveButton @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockWoodenButton(blockstate) {
    override val name: String
        get() = "Mangrove Button"

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(
            BlockID.MANGROVE_BUTTON,
            CommonBlockProperties.BUTTON_PRESSED_BIT,
            CommonBlockProperties.FACING_DIRECTION
        )

    }
}