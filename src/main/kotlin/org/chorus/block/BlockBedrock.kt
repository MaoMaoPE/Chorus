package org.chorus.block

import org.chorus.Player
import org.chorus.block.property.CommonBlockProperties
import org.chorus.block.property.type.BooleanPropertyType
import org.chorus.item.Item
import org.chorus.math.BlockFace
import org.chorus.math.Vector3

class BlockBedrock @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockSolid(blockstate) {
    var burnIndefinitely: Boolean
        get() = getPropertyValue<Boolean, BooleanPropertyType>(CommonBlockProperties.INFINIBURN_BIT)
        set(infiniburn) {
            setPropertyValue<Boolean, BooleanPropertyType>(
                CommonBlockProperties.INFINIBURN_BIT,
                infiniburn
            )
        }

    override val hardness: Double
        get() = -1.0

    override val resistance: Double
        get() = 18000000.0

    override val name: String
        get() = "Bedrock"

    override fun isBreakable(vector: Vector3, layer: Int, face: BlockFace?, item: Item?, player: Player?): Boolean {
        return player != null && player.isCreative
    }

    override fun canBePushed(): Boolean {
        return false
    }

    override fun canBePulled(): Boolean {
        return false
    }

    override fun canHarvestWithHand(): Boolean {
        return false
    }

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.BEDROCK, CommonBlockProperties.INFINIBURN_BIT)
    }
}
