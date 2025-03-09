package org.chorus.level.updater.item

import org.chorus.level.updater.Updater
import org.chorus.level.updater.util.tagupdater.CompoundTagEditHelper
import org.chorus.level.updater.util.tagupdater.CompoundTagUpdaterContext
import java.util.function.Function
import kotlin.collections.Map
import kotlin.collections.get
import kotlin.collections.set

class ItemUpdater_1_20_80 : Updater {
    override fun registerUpdaters(ctx: CompoundTagUpdaterContext) {
        this.addTypeUpdater(
            ctx, "minecraft:sapling", "sapling_type"
        ) { type: String -> "minecraft:" + type + "_sapling" }
        this.addTypeUpdater(
            ctx, "minecraft:red_flower", "flower_type"
        ) { type: String ->
            when (type) {
                "tulip_orange" -> "minecraft:orange_tulip"
                "tulip_pink" -> "minecraft:pink_tulip"
                "tulip_white" -> "minecraft:white_tulip"
                "tulip_red" -> "minecraft:red_tulip"
                "oxeye" -> "minecraft:oxeye_daisy"
                "orchid" -> "minecraft:blue_orchid"
                "houstonia" -> "minecraft:azure_bluet"
                else -> "minecraft:$type"
            }
        }
        this.addTypeUpdater(
            ctx, "minecraft:coral_fan", "coral_color"
        ) { type: String? ->
            when (type) {
                "blue" -> "minecraft:tube_coral_fan"
                "pink" -> "minecraft:brain_coral_fan"
                "purple" -> "minecraft:bubble_coral_fan"
                "yellow" -> "minecraft:horn_coral_fan"
                else -> "minecraft:fire_coral_fan"
            }
        }
        this.addTypeUpdater(
            ctx, "minecraft:coral_fan_dead", "coral_color"
        ) { type: String? ->
            when (type) {
                "blue" -> "minecraft:dead_tube_coral_fan"
                "pink" -> "minecraft:dead_brain_coral_fan"
                "purple" -> "minecraft:dead_bubble_coral_fan"
                "yellow" -> "minecraft:dead_horn_coral_fan"
                else -> "minecraft:dead_fire_coral_fan"
            }
        }
    }

    private fun addTypeUpdater(
        context: CompoundTagUpdaterContext,
        identifier: String,
        typeState: String,
        rename: Function<String, String>
    ) {
        context.addUpdater(1, 20, 80)
            .match("Name", identifier)
            .edit("Name") { helper: CompoundTagEditHelper ->
                val block = helper.rootTag["Block"]
                if (block is Map<*, *>) {
                    val states = block["states"]
                    if (states is Map<*, *>) {
                        val tag = states[typeState]
                        if (tag is String) {
                            helper.rootTag["Name"] = rename.apply(tag)
                        }
                    }
                }
            }
    }

    companion object {
        val INSTANCE: Updater = ItemUpdater_1_20_80()
    }
}
