package org.chorus_oss.chorus.entity.mob.monster

import org.chorus_oss.chorus.block.BlockID
import org.chorus_oss.chorus.entity.Entity
import org.chorus_oss.chorus.entity.EntityID
import org.chorus_oss.chorus.entity.EntityVariant
import org.chorus_oss.chorus.entity.EntityWalkable
import org.chorus_oss.chorus.entity.ai.behavior.Behavior
import org.chorus_oss.chorus.entity.ai.behavior.IBehavior
import org.chorus_oss.chorus.entity.ai.behaviorgroup.BehaviorGroup
import org.chorus_oss.chorus.entity.ai.behaviorgroup.IBehaviorGroup
import org.chorus_oss.chorus.entity.ai.controller.HoppingController
import org.chorus_oss.chorus.entity.ai.controller.IController
import org.chorus_oss.chorus.entity.ai.controller.LookController
import org.chorus_oss.chorus.entity.ai.evaluator.EntityCheckEvaluator
import org.chorus_oss.chorus.entity.ai.executor.FlatRandomRoamExecutor
import org.chorus_oss.chorus.entity.ai.executor.MeleeAttackExecutor
import org.chorus_oss.chorus.entity.ai.memory.CoreMemoryTypes
import org.chorus_oss.chorus.entity.ai.route.finder.impl.SimpleFlatAStarRouteFinder
import org.chorus_oss.chorus.entity.ai.route.posevaluator.WalkingPosEvaluator
import org.chorus_oss.chorus.entity.ai.sensor.ISensor
import org.chorus_oss.chorus.entity.ai.sensor.NearestTargetEntitySensor
import org.chorus_oss.chorus.entity.mob.EntityGolem
import org.chorus_oss.chorus.entity.mob.animal.EntityFrog
import org.chorus_oss.chorus.event.entity.EntityDamageByEntityEvent
import org.chorus_oss.chorus.item.Item
import org.chorus_oss.chorus.item.ItemID
import org.chorus_oss.chorus.level.format.IChunk
import org.chorus_oss.chorus.nbt.tag.CompoundTag
import org.chorus_oss.chorus.utils.Utils
import java.util.function.Function

class EntityMagmaCube(chunk: IChunk?, nbt: CompoundTag?) : EntityMonster(chunk, nbt!!), EntityWalkable, EntityVariant {
    override fun getEntityIdentifier(): String {
        return EntityID.MAGMA_CUBE
    }

    override var variant: Int
        get() = super<EntityVariant>.variant
        set(value) {
            super<EntityVariant>.variant = value
        }

    public override fun requireBehaviorGroup(): IBehaviorGroup {
        return BehaviorGroup(
            this.tickSpread,
            setOf<IBehavior>(),
            setOf<IBehavior>(
                Behavior(
                    MeleeAttackExecutor(CoreMemoryTypes.ATTACK_TARGET, 0.3f, 40, true, 30),
                    EntityCheckEvaluator(CoreMemoryTypes.ATTACK_TARGET),
                    3,
                    1
                ),
                Behavior(
                    MeleeAttackExecutor(
                        CoreMemoryTypes.NEAREST_SUITABLE_ATTACK_TARGET,
                        0.3f,
                        40,
                        false,
                        30
                    ), EntityCheckEvaluator(CoreMemoryTypes.NEAREST_SUITABLE_ATTACK_TARGET), 2, 1
                ),
                Behavior(FlatRandomRoamExecutor(0.3f, 12, 100, false, -1, true, 10), none(), 1, 1)
            ),
            setOf<ISensor>(
                NearestTargetEntitySensor<Entity>(
                    0.0, 16.0, 20,
                    listOf(CoreMemoryTypes.NEAREST_SUITABLE_ATTACK_TARGET),
                    Function<Entity, Boolean> { entity: Entity -> this.attackTarget(entity) })
            ),
            setOf<IController>(HoppingController(40), LookController(true, true)),
            SimpleFlatAStarRouteFinder(WalkingPosEvaluator(), this),
            this
        )
    }

    override fun initEntity() {
        super.initEntity()
        if (!hasVariant()) {
            this.variant = (randomVariant())
        }

        if (variant == SIZE_BIG) {
            this.diffHandDamage = floatArrayOf(4f, 6f, 9f)
        } else if (variant == SIZE_MEDIUM) {
            this.diffHandDamage = floatArrayOf(3f, 4f, 6f)
        } else {
            this.diffHandDamage = floatArrayOf(2.5f, 3f, 4.5f)
        }
        if (variant == SIZE_BIG) {
            this.maxHealth = 16
        } else if (variant == SIZE_MEDIUM) {
            this.maxHealth = 4
        } else if (variant == SIZE_SMALL) {
            this.maxHealth = 1
        }
        setHealthSafe(maxHealth.toFloat())
        recalculateBoundingBox()
    }

    override fun getFloatingForceFactor(): Double {
        return 0.0
    }

    override fun getWidth(): Float {
        return 0.51f + variant * 0.51f
    }

    override fun getHeight(): Float {
        return 0.51f + variant * 0.51f
    }


    override fun getFrostbiteInjury(): Int {
        return 5
    }

    override fun getOriginalName(): String {
        return "Magma Cube"
    }

    override fun getDrops(): Array<Item> {
        if (getLastDamageCause() != null) {
            if (lastDamageCause is EntityDamageByEntityEvent) {
                val damager = (lastDamageCause as EntityDamageByEntityEvent).damager
                if (damager is EntityFrog) {
                    if (variant == SIZE_SMALL) {
                        return arrayOf(
                            Item.get(
                                if (damager.variant == 0) BlockID.OCHRE_FROGLIGHT
                                else if (damager.variant == 1) BlockID.VERDANT_FROGLIGHT
                                else BlockID.PEARLESCENT_FROGLIGHT
                            )
                        )
                    }
                }
            }
        }
        if (variant != SIZE_SMALL) {
            if (Utils.rand(0, 4) == 0) {
                return arrayOf(Item.get(ItemID.MAGMA_CREAM))
            }
        }
        return Item.EMPTY_ARRAY
    }

    override fun getExperienceDrops(): Int {
        return variant
    }

    override fun getAllVariant(): IntArray {
        return intArrayOf(1, 2, 4)
    }

    private fun getSmaller(): Int {
        return when (variant) {
            4 -> 2
            else -> variant - 1
        }
    }

    override fun kill() {
        if (variant != SIZE_SMALL) {
            for (i in 1..<Utils.rand(2, 5)) {
                val magmaCube = EntityMagmaCube(this.locator.chunk, this.namedTag)
                magmaCube.setPosition(position.add(Utils.rand(-0.5, 0.5), 0.0, Utils.rand(-0.5, 0.5)))
                magmaCube.setRotation(rotation.yaw, rotation.pitch)
                magmaCube.variant = (getSmaller())
                magmaCube.spawnToAll()
            }
        }
        super.kill()
    }

    override fun attackTarget(entity: Entity): Boolean {
        return super.attackTarget(entity) || entity is EntityGolem
    }

    companion object {
        const val SIZE_SMALL: Int = 1
        const val SIZE_MEDIUM: Int = 2
        const val SIZE_BIG: Int = 4
    }
}
