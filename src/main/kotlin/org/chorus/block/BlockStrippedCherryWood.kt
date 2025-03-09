package org.chorus.block

import org.chorus.Player
import org.chorus.block.property.CommonBlockProperties
import org.chorus.block.property.enums.WoodType
import org.chorus.item.Item
import org.chorus.math.BlockFace

class BlockStrippedCherryWood @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockWoodStripped(blockstate) {
    override val name: String
        get() = "Stripped Cherry Wood"

    override val hardness: Double
        get() = 2.0

    override val resistance: Double
        get() = 10.0

    override val burnChance: Int
        get() = 5

    override val burnAbility: Int
        get() = 5

    override fun getStrippedState(): BlockState {
        return blockState!!
    }

    override fun canBeActivated(): Boolean {
        return false
    }

    override fun onActivate(
        item: Item,
        player: Player?,
        blockFace: BlockFace?,
        fx: Float,
        fy: Float,
        fz: Float
    ): Boolean {
        return false
    }

    override fun getWoodType(): WoodType {
        throw UnsupportedOperationException()
    }

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.STRIPPED_CHERRY_WOOD, CommonBlockProperties.PILLAR_AXIS)
            get() = Companion.field
    }
}