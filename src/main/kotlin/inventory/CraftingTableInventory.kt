package org.chorus_oss.chorus.inventory

import org.chorus_oss.chorus.Player
import org.chorus_oss.chorus.block.BlockCraftingTable
import org.chorus_oss.chorus.network.protocol.ContainerOpenPacket
import org.chorus_oss.chorus.network.protocol.types.itemstack.ContainerSlotType
import org.chorus_oss.chorus.recipe.Input

class CraftingTableInventory(table: BlockCraftingTable) : BaseInventory(table, InventoryType.WORKBENCH, 9),
    CraftTypeInventory, InputInventory {
    override fun init() {
        val map = super.networkSlotMap()
        for (i in 0..<size) {
            map[i] = 32 + i
        }

        val map2 = super.slotTypeMap()
        for (i in 0..<size) {
            map2[i] = ContainerSlotType.CRAFTING_INPUT
            map2[i + 32] = ContainerSlotType.CRAFTING_INPUT
        }
    }

    override fun onOpen(who: Player) {
        super.onOpen(who)
        who.dataPacket(
            ContainerOpenPacket(
                containerID = who.getWindowId(this),
                containerType = type.networkType,
                position = holder.vector3.asBlockVector3(),
                targetActorID = who.getRuntimeID()
            )
        )
        this.sendContents(who)
    }

    override var holder: InventoryHolder
        get() = super.holder as BlockCraftingTable
        set(holder) {
            super.holder = holder
        }

    override val input: Input
        get() {
            val item1 = arrayOf(getItem(0), getItem(1), getItem(2))
            val item2 = arrayOf(getItem(3), getItem(4), getItem(5))
            val item3 = arrayOf(getItem(6), getItem(7), getItem(8))
            val items = arrayOf(item1, item2, item3)
            return Input(3, 3, items)
        }
}
