package org.chorus.entity.ai.sensor

import org.chorus.block.*
import org.chorus.entity.*
import org.chorus.entity.ai.memory.CoreMemoryTypes
import org.chorus.entity.mob.EntityMob
import org.chorus.inventory.*


//存储最近的玩家的Memory

class NearestPlayerAngryPiglinSensor : ISensor {
    override fun sense(entity: EntityMob) {
        for (player in entity.viewers.values) {
            if (player.position.distance(entity.position) < 32) {
                var trigger = false
                if (player.topWindow.isPresent) {
                    if (checkInventory(player.topWindow.get())) {
                        trigger = true
                    }
                }
                if (player.isBreakingBlock) {
                    if (checkBlock(player.breakingBlock)) {
                        trigger = true
                    }
                }
                if (trigger) {
                    entity.memoryStorage!!
                        .put<Entity>(CoreMemoryTypes.Companion.ATTACK_TARGET, player)
                }
            }
        }
    }

    override val period: Int
        get() = 1

    private fun checkInventory(inventory: Inventory): Boolean {
        return inventory is ChestInventory ||
                inventory is DoubleChestInventory ||
                inventory is HumanEnderChestInventory ||
                inventory is ShulkerBoxInventory ||
                inventory is BarrelInventory ||
                inventory is MinecartChestInventory ||
                inventory is ChestBoatInventory
    }

    private fun checkBlock(block: Block): Boolean {
        return when (block.id) {
            Block.GOLD_BLOCK, Block.GOLD_ORE, Block.GILDED_BLACKSTONE, Block.NETHER_GOLD_ORE, Block.RAW_GOLD_BLOCK, Block.DEEPSLATE_GOLD_ORE -> true
            else -> false
        }
    }
}
