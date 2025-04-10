package org.chorus.inventory

import org.chorus.blockentity.BlockEntityFurnace
import org.chorus.network.protocol.types.itemstack.ContainerSlotType

class BlastFurnaceInventory(furnace: BlockEntityFurnace) : SmeltingInventory(furnace, InventoryType.BLAST_FURNACE, 3) {
    override fun init() {
        val map = super.slotTypeMap()
        map[0] = ContainerSlotType.BLAST_FURNACE_INGREDIENT
        map[1] = ContainerSlotType.FURNACE_FUEL
        map[2] = ContainerSlotType.FURNACE_RESULT
    }
}
