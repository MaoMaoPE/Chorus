package org.chorus.dispenser

import org.chorus.block.BlockDispenser
import org.chorus.entity.Entity
import org.chorus.entity.item.EntityTnt
import org.chorus.item.Item
import org.chorus.math.BlockFace

class TNTDispenseBehavior : DefaultDispenseBehavior() {
    override fun dispense(block: BlockDispenser, face: BlockFace, item: Item): Item? {
        val pos = block.getSide(face).position.add(0.5, 0.0, 0.5)

        val tnt = EntityTnt(
            block.level.getChunk(pos.chunkX, pos.chunkZ),
            Entity.getDefaultNBT(pos)
        )
        tnt.spawnToAll()

        return null
    }
}
