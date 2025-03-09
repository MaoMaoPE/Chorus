package org.chorus.level.generator.terra.mappings.populator

import cn.nukkit.block.*
import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.level.generator.terra.mappings.BlockMappings
import cn.nukkit.level.generator.terra.mappings.JeBlockState
import cn.nukkit.level.generator.terra.mappings.populator.BlockRegistryPopulator.Remapper
import cn.nukkit.level.updater.Updater
import cn.nukkit.level.updater.block.*
import cn.nukkit.level.updater.util.tagupdater.CompoundTagUpdaterContext
import cn.nukkit.nbt.tag.CompoundTag
import cn.nukkit.nbt.tag.TreeMapCompoundTag
import cn.nukkit.registry.Registries
import cn.nukkit.utils.*
import com.google.gson.reflect.TypeToken
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap
import java.io.IOException
import kotlin.collections.Map
import kotlin.collections.Set
import kotlin.collections.set

/**
 * Populates the block registries.
 */
object BlockRegistryPopulator {
    val mapper: Remapper = Remapper.of(
        BlockStateUpdater_1_20_10.Companion.INSTANCE,
        BlockStateUpdater_1_20_30.Companion.INSTANCE,
        BlockStateUpdater_1_20_40.Companion.INSTANCE,
        BlockStateUpdater_1_20_50.Companion.INSTANCE,
        BlockStateUpdater_1_20_60.Companion.INSTANCE,
        BlockStateUpdater_1_20_70.Companion.INSTANCE,
        BlockStateUpdater_1_20_80.Companion.INSTANCE,
        BlockStateUpdater_1_21_0.Companion.INSTANCE,
        BlockStateUpdater_1_21_10.Companion.INSTANCE,
        BlockStateUpdater_1_21_20.Companion.INSTANCE,
        BlockStateUpdater_1_21_30.Companion.INSTANCE,
        BlockStateUpdater_1_21_40.Companion.INSTANCE
    )

    fun initMapping(): BlockMappings {
        try {
            BlockRegistryPopulator::class.java.classLoader.getResourceAsStream("mappings/blocks.json").use { stream ->
                val blocks: Map<String, Map<String, Any>> = JSONUtils.from<Map<String, Map<String, Any>>>(
                    stream,
                    object : TypeToken<Map<String?, Map<String?, Any?>?>?>() {
                    }
                )
                val MAP1 = Object2ObjectOpenHashMap<JeBlockState, BlockState?>()
                val MAP2 = Object2ObjectOpenHashMap<BlockState?, JeBlockState>()
                blocks.forEach { k: String, v: Map<String, Any> ->
                    val treeMapCompoundTag = TreeMapCompoundTag()
                    val name = v["bedrock_identifier"].toString()
                    treeMapCompoundTag.putString("name", name)
                    val stateTag = TreeMapCompoundTag()
                    if (v.containsKey("bedrock_states")) {
                        val states = v["bedrock_states"] as Map<String, Any>?
                        states!!.forEach { key: String?, value: Any ->
                            val valueString = value.toString()
                            if (valueString == "true" || valueString == "false") {
                                stateTag.putBoolean(key, java.lang.Boolean.parseBoolean(valueString))
                            } else if (value is Number) {
                                stateTag.putInt(key, value.intValue())
                            } else {
                                stateTag.putString(key, value.toString())
                            }
                        }
                    }
                    treeMapCompoundTag.putCompound("states", stateTag)
                    treeMapCompoundTag.putString("version", "18087936")

                    val remappedTag = mapper.remap(treeMapCompoundTag)
                    val i = HashUtils.fnv1a_32_nbt(remappedTag)
                    var pnxBlockState = Registries.BLOCKSTATE[i]
                    if (pnxBlockState == null && !experimentalBlocks.contains(remappedTag.getString("name"))) {
                        pnxBlockState = BlockAir.STATE
                    }
                    val jeBlockState = JeBlockState(k)
                    MAP1[jeBlockState] = pnxBlockState
                    MAP2[pnxBlockState] = jeBlockState
                }
                val i = BlockFlowingLava.properties.getBlockState(CommonBlockProperties.LIQUID_DEPTH.createValue(0))
                val jeBlockState = JeBlockState("minecraft:lava[level=0]")
                MAP1[jeBlockState] = i
                MAP2[i] = jeBlockState
                MAP1.trim()
                MAP2.trim()
                return BlockMappings.builder().mapping1(MAP1).mapping2(MAP2).build()
            }
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

    val experimentalBlocks: Set<String> = java.util.Set.of(
        "minecraft:waxed_oxidized_copper_grate",
        "minecraft:hard_glass",
        "minecraft:element_38",
        "minecraft:polished_tuff_slab",
        "minecraft:element_37",
        "minecraft:element_39",
        "minecraft:chemical_heat",
        "minecraft:element_34",
        "minecraft:element_33",
        "minecraft:waxed_exposed_copper_grate",
        "minecraft:element_36",
        "minecraft:element_35",
        "minecraft:element_30",
        "minecraft:element_32",
        "minecraft:element_31",
        "minecraft:waxed_oxidized_copper_door",
        "minecraft:waxed_weathered_copper_bulb",
        "minecraft:waxed_copper_bulb",
        "minecraft:exposed_copper_bulb",
        "minecraft:weathered_copper_trapdoor",
        "minecraft:waxed_exposed_copper_bulb",
        "minecraft:element_27",
        "minecraft:element_26",
        "minecraft:chiseled_tuff_bricks",
        "minecraft:element_29",
        "minecraft:element_28",
        "minecraft:weathered_copper_grate",
        "minecraft:element_23",
        "minecraft:element_22",
        "minecraft:element_25",
        "minecraft:element_24",
        "minecraft:element_118",
        "minecraft:element_21",
        "minecraft:element_20",
        "minecraft:polished_tuff_double_slab",
        "minecraft:waxed_copper_trapdoor",
        "minecraft:element_112",
        "minecraft:element_113",
        "minecraft:weathered_copper_bulb",
        "minecraft:waxed_chiseled_copper",
        "minecraft:element_110",
        "minecraft:element_111",
        "minecraft:element_116",
        "minecraft:element_117",
        "minecraft:weathered_chiseled_copper",
        "minecraft:element_114",
        "minecraft:element_115",
        "minecraft:hard_stained_glass_pane",
        "minecraft:waxed_exposed_copper_trapdoor",
        "minecraft:element_19",
        "minecraft:element_16",
        "minecraft:element_15",
        "minecraft:tuff_slab",
        "minecraft:element_18",
        "minecraft:element_17",
        "minecraft:element_12",
        "minecraft:oxidized_copper_bulb",
        "minecraft:element_11",
        "minecraft:element_99",
        "minecraft:element_14",
        "minecraft:element_13",
        "minecraft:element_96",
        "minecraft:element_95",
        "minecraft:element_10",
        "minecraft:element_98",
        "minecraft:element_97",
        "minecraft:element_92",
        "minecraft:waxed_exposed_chiseled_copper",
        "minecraft:element_91",
        "minecraft:element_94",
        "minecraft:element_93",
        "minecraft:tuff_bricks",
        "minecraft:element_90",
        "minecraft:copper_grate",
        "minecraft:hard_stained_glass",
        "minecraft:element_89",
        "minecraft:element_88",
        "minecraft:colored_torch_bp",
        "minecraft:tuff_brick_slab",
        "minecraft:element_85",
        "minecraft:tuff_double_slab",
        "minecraft:element_84",
        "minecraft:polished_tuff_stairs",
        "minecraft:element_87",
        "minecraft:element_86",
        "minecraft:element_81",
        "minecraft:element_80",
        "minecraft:element_83",
        "minecraft:colored_torch_rg",
        "minecraft:element_82",
        "minecraft:waxed_oxidized_copper_bulb",
        "minecraft:hard_glass_pane",
        "minecraft:waxed_exposed_copper_door",
        "minecraft:chiseled_tuff",
        "minecraft:polished_tuff_wall",
        "minecraft:waxed_oxidized_copper_trapdoor",
        "minecraft:waxed_oxidized_chiseled_copper",
        "minecraft:element_78",
        "minecraft:tuff_stairs",
        "minecraft:element_77",
        "minecraft:waxed_weathered_chiseled_copper",
        "minecraft:element_79",
        "minecraft:element_74",
        "minecraft:element_73",
        "minecraft:element_76",
        "minecraft:element_75",
        "minecraft:element_70",
        "minecraft:waxed_copper_grate",
        "minecraft:element_72",
        "minecraft:element_71",
        "minecraft:waxed_weathered_copper_trapdoor",
        "minecraft:exposed_copper_grate",
        "minecraft:tuff_brick_double_slab",
        "minecraft:chemistry_table",
        "minecraft:oxidized_copper_door",
        "minecraft:element_67",
        "minecraft:copper_bulb",
        "minecraft:element_66",
        "minecraft:element_69",
        "minecraft:element_68",
        "minecraft:element_63",
        "minecraft:element_62",
        "minecraft:element_65",
        "minecraft:tuff_brick_stairs",
        "minecraft:element_64",
        "minecraft:exposed_copper_door",
        "minecraft:element_61",
        "minecraft:element_60",
        "minecraft:waxed_weathered_copper_door",
        "minecraft:crafter",
        "minecraft:element_59",
        "minecraft:element_56",
        "minecraft:element_55",
        "minecraft:element_58",
        "minecraft:exposed_chiseled_copper",
        "minecraft:element_57",
        "minecraft:element_109",
        "minecraft:element_52",
        "minecraft:element_51",
        "minecraft:element_107",
        "minecraft:element_54",
        "minecraft:oxidized_copper_trapdoor",
        "minecraft:element_108",
        "minecraft:element_53",
        "minecraft:weathered_copper_door",
        "minecraft:element_50",
        "minecraft:tuff_wall",
        "minecraft:element_101",
        "minecraft:element_102",
        "minecraft:exposed_copper_trapdoor",
        "minecraft:element_100",
        "minecraft:oxidized_chiseled_copper",
        "minecraft:element_105",
        "minecraft:element_106",
        "minecraft:element_103",
        "minecraft:element_104",
        "minecraft:tuff_brick_wall",
        "minecraft:underwater_torch",
        "minecraft:element_49",
        "minecraft:element_48",
        "minecraft:element_45",
        "minecraft:element_44",
        "minecraft:element_47",
        "minecraft:element_46",
        "minecraft:element_2",
        "minecraft:element_41",
        "minecraft:copper_trapdoor",
        "minecraft:element_3",
        "minecraft:element_40",
        "minecraft:element_0",
        "minecraft:element_43",
        "minecraft:waxed_weathered_copper_grate",
        "minecraft:element_1",
        "minecraft:element_42",
        "minecraft:element_6",
        "minecraft:camera",
        "minecraft:element_7",
        "minecraft:element_4",
        "minecraft:element_5",
        "minecraft:chiseled_copper",
        "minecraft:oxidized_copper_grate",
        "minecraft:element_8",
        "minecraft:element_9",
        "minecraft:waxed_copper_door"
    )

    fun interface Remapper {
        fun remap(tag: CompoundTag?): CompoundTag

        companion object {
            fun of(vararg updaters: Updater): Remapper {
                val context = CompoundTagUpdaterContext()
                for (updater in updaters) {
                    updater.registerUpdaters(context)
                }

                return Remapper { tag: CompoundTag ->
                    val updated = context.update(tag, 0)
                    updated!!.remove("version") // we already removed this, but the context adds it. remove it again.
                    TreeMapCompoundTag(updated.tags)
                }
            }
        }
    }
}
