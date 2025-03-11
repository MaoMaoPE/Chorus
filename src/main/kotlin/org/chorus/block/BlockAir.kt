package org.chorus.block

import org.chorus.Player
import org.chorus.item.*
import org.chorus.math.BlockFace
import org.chorus.math.Vector3


class BlockAir : BlockTransparent {
    constructor() : super(Companion.properties.defaultState)

    constructor(blockState: BlockState?) : super(blockState)

    override val name: String
        get() = "Air"

    override fun canPassThrough(): Boolean {
        return true
    }

    override fun isBreakable(vector: Vector3, layer: Int, face: BlockFace?, item: Item?, player: Player?): Boolean {
        return false
    }

    override fun canBeFlowedInto(): Boolean {
        return true
    }

    override fun canBePlaced(): Boolean {
        return false
    }

    override fun canBeReplaced(): Boolean {
        return true
    }

    override val isSolid: Boolean
        get() = false

    override fun isSolid(side: BlockFace): Boolean {
        return false
    }

    override val hardness: Double
        get() = 0.0

    override val resistance: Double
        get() = 0.0

    override fun canHarvestWithHand(): Boolean {
        return false
    }

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.AIR)
        val STATE: BlockState = properties.defaultState
    }
}
