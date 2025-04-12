package org.chorus.entity.mob.monster

import org.chorus.Player
import org.chorus.Server
import org.chorus.block.BlockID
import org.chorus.entity.Entity
import org.chorus.entity.EntityID
import org.chorus.entity.EntitySwimmable
import org.chorus.entity.ai.behavior.Behavior
import org.chorus.entity.ai.behavior.IBehavior
import org.chorus.entity.ai.behaviorgroup.BehaviorGroup
import org.chorus.entity.ai.behaviorgroup.IBehaviorGroup
import org.chorus.entity.ai.controller.DiveController
import org.chorus.entity.ai.controller.IController
import org.chorus.entity.ai.controller.LookController
import org.chorus.entity.ai.controller.SpaceMoveController
import org.chorus.entity.ai.evaluator.EntityCheckEvaluator
import org.chorus.entity.ai.evaluator.IBehaviorEvaluator
import org.chorus.entity.ai.evaluator.PassByTimeEvaluator
import org.chorus.entity.ai.evaluator.RandomSoundEvaluator
import org.chorus.entity.ai.executor.FleeFromTargetExecutor
import org.chorus.entity.ai.executor.GuardianAttackExecutor
import org.chorus.entity.ai.executor.PlaySoundExecutor
import org.chorus.entity.ai.executor.SpaceRandomRoamExecutor
import org.chorus.entity.ai.memory.CoreMemoryTypes
import org.chorus.entity.ai.route.finder.impl.SimpleSpaceAStarRouteFinder
import org.chorus.entity.ai.route.posevaluator.SwimmingPosEvaluator
import org.chorus.entity.ai.sensor.ISensor
import org.chorus.entity.ai.sensor.NearestPlayerSensor
import org.chorus.entity.ai.sensor.NearestTargetEntitySensor
import org.chorus.entity.data.EntityFlag
import org.chorus.entity.effect.Effect
import org.chorus.entity.effect.EffectType
import org.chorus.entity.mob.EntityMob
import org.chorus.event.entity.EntityDamageByEntityEvent
import org.chorus.event.entity.EntityDamageEvent
import org.chorus.event.entity.EntityDamageEvent.DamageCause
import org.chorus.item.Item
import org.chorus.item.ItemID
import org.chorus.level.Sound
import org.chorus.level.format.IChunk
import org.chorus.nbt.tag.CompoundTag
import org.chorus.network.protocol.LevelEventPacket
import org.chorus.utils.Utils
import java.util.concurrent.ThreadLocalRandom
import java.util.function.Function

class EntityElderGuardian(chunk: IChunk?, nbt: CompoundTag) : EntityMonster(chunk, nbt), EntitySwimmable {
    override fun getEntityIdentifier(): String {
        return EntityID.ELDER_GUARDIAN
    }

    public override fun requireBehaviorGroup(): IBehaviorGroup {
        return BehaviorGroup(
            this.tickSpread,
            setOf<IBehavior>(),
            setOf<IBehavior>(
                Behavior(
                    PlaySoundExecutor(Sound.MOB_ELDERGUARDIAN_IDLE, 0.8f, 1.2f, 1f, 1f),
                    all(IBehaviorEvaluator { entity: EntityMob? -> isInsideOfWater() }, RandomSoundEvaluator()),
                    6,
                    1,
                    1,
                    true
                ),
                Behavior(
                    PlaySoundExecutor(Sound.MOB_GUARDIAN_LAND_IDLE, 0.8f, 1.2f, 1f, 1f),
                    all(IBehaviorEvaluator { entity: EntityMob? -> !isInsideOfWater() }, RandomSoundEvaluator()),
                    5,
                    1,
                    1,
                    true
                ),
                Behavior(
                    FleeFromTargetExecutor(CoreMemoryTypes.Companion.ATTACK_TARGET, 0.5f, true, 9f), all(
                        EntityCheckEvaluator(CoreMemoryTypes.Companion.ATTACK_TARGET),
                        PassByTimeEvaluator(CoreMemoryTypes.Companion.LAST_BE_ATTACKED_TIME, 0, 100)
                    ), 4, 1
                ),
                Behavior(
                    GuardianAttackExecutor(CoreMemoryTypes.Companion.NEAREST_PLAYER, 0.3f, 15, true, 60, 40), all(
                        EntityCheckEvaluator(CoreMemoryTypes.Companion.NEAREST_PLAYER),
                        IBehaviorEvaluator { entity: EntityMob ->
                            entity.memoryStorage.get(CoreMemoryTypes.Companion.NEAREST_PLAYER) != null && !entity.memoryStorage
                                .get<Player>(CoreMemoryTypes.Companion.NEAREST_PLAYER)!!.isBlocking()
                        },
                        IBehaviorEvaluator { entity: EntityMob ->
                            entity.memoryStorage[CoreMemoryTypes.Companion.NEAREST_PLAYER] != null && level!!.raycastBlocks(
                                entity.position, entity.memoryStorage
                                    .get<Player>(CoreMemoryTypes.Companion.NEAREST_PLAYER)!!.position
                            ).stream().allMatch { obj -> obj.isTransparent }
                        }
                    ), 3, 1),
                Behavior(
                    GuardianAttackExecutor(
                        CoreMemoryTypes.Companion.NEAREST_SUITABLE_ATTACK_TARGET,
                        0.3f,
                        15,
                        true,
                        60,
                        40
                    ), EntityCheckEvaluator(CoreMemoryTypes.Companion.NEAREST_SUITABLE_ATTACK_TARGET), 2, 1
                ),
                Behavior(SpaceRandomRoamExecutor(0.36f, 12, 1, 80, false, -1, false, 10), none(), 1, 1)
            ),
            setOf<ISensor>(
                NearestPlayerSensor(40.0, 0.0, 20),
                NearestTargetEntitySensor<Entity>(
                    0.0, 16.0, 20,
                    listOf(CoreMemoryTypes.Companion.NEAREST_SUITABLE_ATTACK_TARGET),
                    Function<Entity, Boolean> { entity: Entity? ->
                        this.attackTarget(
                            entity!!
                        )
                    })
            ),
            setOf<IController>(SpaceMoveController(), LookController(true, true), DiveController()),
            SimpleSpaceAStarRouteFinder(SwimmingPosEvaluator(), this),
            this
        )
    }


    override fun initEntity() {
        this.maxHealth = 80
        super.initEntity()
        this.diffHandDamage = floatArrayOf(5f, 8f, 12f)
        this.setDataFlag(EntityFlag.ELDER, true)
    }

    override fun getWidth(): Float {
        return 1.99f
    }

    override fun getHeight(): Float {
        return 1.99f
    }

    override fun getOriginalName(): String {
        return "Elder Guardian"
    }

    override fun isPreventingSleep(player: Player?): Boolean {
        return true
    }

    override fun getDrops(): Array<Item> {
        val secondLoot = ThreadLocalRandom.current().nextInt(6)
        return arrayOf(
            Item.get(ItemID.PRISMARINE_SHARD, 0, Utils.rand(0, 2)),
            Item.get(BlockID.WET_SPONGE, 0, 1),
            if (ThreadLocalRandom.current().nextInt(100) < 20) Item.get(
                ItemID.TIDE_ARMOR_TRIM_SMITHING_TEMPLATE,
                0,
                1
            ) else Item.AIR,
            if (ThreadLocalRandom.current().nextInt(1000) < 25) Item.get(ItemID.COD, 0, 1) else Item.AIR,
            if (secondLoot <= 2) Item.get(ItemID.COD, 0, Utils.rand(0, 1)) else Item.AIR,
            if (secondLoot > 2 && secondLoot <= 4) Item.get(
                ItemID.PRISMARINE_CRYSTALS,
                0,
                Utils.rand(0, 1)
            ) else Item.AIR
        )
    }

    override fun attack(source: EntityDamageEvent): Boolean {
        if (source.cause == DamageCause.SUFFOCATION) {
            return false
        }
        if (super.attack(source)) {
            if (source is EntityDamageByEntityEvent) {
                source.damager.attack(
                    EntityDamageByEntityEvent(
                        this,
                        source.entity,
                        DamageCause.THORNS,
                        (if (Server.instance.getDifficulty() == 3) 2 else 3).toFloat()
                    )
                )
            }
            return true
        }
        return false
    }

    override fun onUpdate(currentTick: Int): Boolean {
        if (!this.closed && this.isAlive()) {
            for (p in this.viewers.values) {
                if (p.locallyInitialized && p.gamemode % 2 == 0 && p.position.distance(this.position) < 50 && !p.hasEffect(
                        EffectType.MINING_FATIGUE
                    )
                ) {
                    p.addEffect(
                        Effect.get(EffectType.MINING_FATIGUE).setAmplifier(2).setDuration(6000)
                    )
                    val pk = LevelEventPacket()
                    pk.evid = LevelEventPacket.EVENT_PARTICLE_SOUND_GUARDIAN_GHOST
                    pk.x = position.x.toFloat()
                    pk.y = position.y.toFloat()
                    pk.z = position.z.toFloat()
                    p.dataPacket(pk)
                }
            }
        }
        return super.onUpdate(currentTick)
    }

    override fun getExperienceDrops(): Int {
        return 10
    }

    override fun attackTarget(entity: Entity): Boolean {
        return when (entity.getEntityIdentifier()) {
            EntityID.SQUID, EntityID.GLOW_SQUID, EntityID.AXOLOTL -> true
            else -> false
        }
    }
}
