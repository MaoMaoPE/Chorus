package org.chorus_oss.chorus.event.entity

import org.chorus_oss.chorus.entity.Entity.Companion.getDefaultNBT
import org.chorus_oss.chorus.event.Cancellable
import org.chorus_oss.chorus.event.Event
import org.chorus_oss.chorus.event.HandlerList
import org.chorus_oss.chorus.level.Locator
import org.chorus_oss.chorus.nbt.tag.CompoundTag

class CreatureSpawnEvent : Event, Cancellable {
    val reason: SpawnReason

    @JvmField
    val entityNetworkId: Int
    val position: Locator
    val compoundTag: CompoundTag

    constructor(networkId: Int, locator: Locator, nbt: CompoundTag, reason: SpawnReason) {
        this.reason = reason
        this.entityNetworkId = networkId
        this.position = locator
        this.compoundTag = nbt
    }

    constructor(networkId: Int, locator: Locator, reason: SpawnReason) {
        this.reason = reason
        this.entityNetworkId = networkId
        this.position = locator
        this.compoundTag = getDefaultNBT(locator.position)
    }

    /**
     * An enum to specify the type of spawning
     */
    enum class SpawnReason {
        /**
         * When something spawns from natural means
         */
        NATURAL,

        /**
         * When an entity spawns as a jockey of another entity (mostly spider
         * jockeys)
         */
        JOCKEY,

        /**
         * When a creature spawns from a spawner
         */
        SPAWNER,

        /**
         * When a creature spawns from an egg
         */
        EGG,

        /**
         * When a creature spawns from a Spawner Egg
         */
        SPAWN_EGG,

        /**
         * When a creature spawns because of a lightning strike
         */
        LIGHTNING,

        /**
         * When a snowman is spawned by being built
         */
        BUILD_SNOWMAN,

        /**
         * When an iron golem is spawned by being built
         */
        BUILD_IRONGOLEM,

        /**
         * When a wither boss is spawned by being built
         */
        BUILD_WITHER,

        /**
         * When an iron golem is spawned to defend a village
         */
        VILLAGE_DEFENSE,

        /**
         * When a zombie is spawned to invade a village
         */
        VILLAGE_INVASION,

        /**
         * When an animal breeds to create a child
         */
        BREEDING,

        /**
         * When a slime splits
         */
        SLIME_SPLIT,

        /**
         * When an entity calls for reinforcements
         */
        REINFORCEMENTS,

        /**
         * When a creature is spawned by nether portal
         */
        NETHER_PORTAL,

        /**
         * When a creature is spawned by a dispenser dispensing an egg
         */
        DISPENSE_EGG,

        /**
         * When a zombie infects a villager
         */
        INFECTION,

        /**
         * When a villager is cured from infection
         */
        CURED,

        /**
         * When an ocelot has a baby spawned along with them
         */
        OCELOT_BABY,

        /**
         * When a silverfish spawns from a block
         */
        SILVERFISH_BLOCK,

        /**
         * When an entity spawns as a mount of another entity (mostly chicken
         * jockeys)
         */
        MOUNT,

        /**
         * When an entity spawns as a trap for players approaching
         */
        TRAP,

        /**
         * When an entity is spawned as a result of ender pearl usage
         */
        ENDER_PEARL,

        /**
         * When an entity is spawned as a result of the entity it is being
         * perched on jumping or being damaged
         */
        SHOULDER_ENTITY,

        /**
         * When a creature is spawned by another entity drowning
         */
        DROWNED,

        /**
         * When an cow is spawned by shearing a mushroom cow
         */
        SHEARED,

        /**
         * When a creature is spawned by plugins
         */
        CUSTOM,

        /**
         * When an entity is missing a SpawnReason
         */
        DEFAULT,

        /**
         * When turtles hatches from turtle eggs
         */
        TURTLE_EGG,

        /**
         * When an creaking heart spawns an entity
         */
        CREAKING_HEART
    }

    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
