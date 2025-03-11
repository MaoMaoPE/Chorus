package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockBlastFurnace @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockLitBlastFurnace(blockstate) {
    override val name: String
        get() = "Blast Furnace"

    override val lightLevel: Int
        get() = 0

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.BLAST_FURNACE, CommonBlockProperties.MINECRAFT_CARDINAL_DIRECTION)
    }
}
