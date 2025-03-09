package org.chorus.blockentity

import org.chorus.block.BlockID
import org.chorus.inventory.DropperInventory
import org.chorus.level.format.IChunk
import org.chorus.nbt.tag.CompoundTag

class BlockEntityDropper(chunk: IChunk, nbt: CompoundTag) : BlockEntityEjectable(chunk, nbt) {
    override fun createInventory(): DropperInventory {
        inventory = DropperInventory(this)
        return getInventory()
    }

    override val blockEntityName: String
        get() = BlockEntityID.Companion.DROPPER

    override fun getInventory(): DropperInventory {
        return inventory as DropperInventory
    }

    override val isBlockEntityValid: Boolean
        get() = this.levelBlock.id === BlockID.DROPPER
}
