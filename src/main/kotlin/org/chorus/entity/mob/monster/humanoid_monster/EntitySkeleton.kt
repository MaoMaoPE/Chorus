package org.chorus.entity.mob.monster.humanoid_monster

import org.chorus.Player
import org.chorus.entity.EntityID
import org.chorus.entity.EntitySmite
import org.chorus.entity.EntityWalkable
import org.chorus.entity.ai.behavior.Behavior
import org.chorus.entity.ai.behavior.IBehavior
import org.chorus.entity.ai.behaviorgroup.BehaviorGroup
import org.chorus.entity.ai.behaviorgroup.IBehaviorGroup
import org.chorus.entity.ai.controller.IController
import org.chorus.entity.ai.controller.LookController
import org.chorus.entity.ai.controller.WalkController
import org.chorus.entity.ai.evaluator.EntityCheckEvaluator
import org.chorus.entity.ai.evaluator.RandomSoundEvaluator
import org.chorus.entity.ai.executor.BowShootExecutor
import org.chorus.entity.ai.executor.FlatRandomRoamExecutor
import org.chorus.entity.ai.executor.PlaySoundExecutor
import org.chorus.entity.ai.memory.CoreMemoryTypes
import org.chorus.entity.ai.route.finder.impl.SimpleFlatAStarRouteFinder
import org.chorus.entity.ai.route.posevaluator.WalkingPosEvaluator
import org.chorus.entity.ai.sensor.ISensor
import org.chorus.entity.ai.sensor.NearestEntitySensor
import org.chorus.entity.ai.sensor.NearestPlayerSensor
import org.chorus.entity.mob.EntityGolem
import org.chorus.item.Item
import org.chorus.item.ItemID
import org.chorus.level.Sound
import org.chorus.level.format.IChunk
import org.chorus.nbt.tag.CompoundTag

open class EntitySkeleton(chunk: IChunk?, nbt: CompoundTag?) : EntityHumanoidMonster(chunk, nbt), EntityWalkable,
    EntitySmite {
    override fun getEntityIdentifier(): String {
        return EntityID.SKELETON
    }


    override fun initEntity() {
        this.maxHealth = 20
        super.initEntity()
        if (itemInHand.isNothing) {
            setItemInHand(Item.get(ItemID.BOW))
        }
    }

    override fun getWidth(): Float {
        return 0.6f
    }

    override fun getHeight(): Float {
        return 1.9f
    }

    override fun getOriginalName(): String {
        return "Skeleton"
    }

    override fun getDrops(): Array<Item> {
        return arrayOf(Item.get(ItemID.BONE), Item.get(ItemID.ARROW))
    }

    override fun isUndead(): Boolean {
        return true
    }

    override fun isPreventingSleep(player: Player?): Boolean {
        return true
    }

    override fun onUpdate(currentTick: Int): Boolean {
        burn(this)
        return super.onUpdate(currentTick)
    }

    override fun requireBehaviorGroup(): IBehaviorGroup {
        return BehaviorGroup(
            this.tickSpread,
            setOf<IBehavior>(),
            setOf<IBehavior>(
                Behavior(PlaySoundExecutor(Sound.MOB_SKELETON_SAY), RandomSoundEvaluator(), 5, 1),
                Behavior(
                    BowShootExecutor(
                        { this.itemInHand },
                        CoreMemoryTypes.Companion.ATTACK_TARGET,
                        0.3f,
                        15,
                        true,
                        30,
                        20
                    ), EntityCheckEvaluator(CoreMemoryTypes.Companion.ATTACK_TARGET), 4, 1
                ),
                Behavior(
                    BowShootExecutor(
                        { this.itemInHand },
                        CoreMemoryTypes.Companion.NEAREST_GOLEM,
                        0.3f,
                        15,
                        true,
                        30,
                        20
                    ), EntityCheckEvaluator(CoreMemoryTypes.Companion.NEAREST_GOLEM), 3, 1
                ),
                Behavior(
                    BowShootExecutor(
                        { this.itemInHand },
                        CoreMemoryTypes.Companion.NEAREST_PLAYER,
                        0.3f,
                        15,
                        true,
                        30,
                        20
                    ), EntityCheckEvaluator(CoreMemoryTypes.Companion.NEAREST_PLAYER), 2, 1
                ),
                Behavior(FlatRandomRoamExecutor(0.3f, 12, 100, false, -1, true, 10), none(), 1, 1)
            ),
            setOf<ISensor>(
                NearestPlayerSensor(16.0, 0.0, 20),
                NearestEntitySensor(EntityGolem::class.java, CoreMemoryTypes.Companion.NEAREST_GOLEM, 42.0, 0.0)
            ),
            setOf<IController>(WalkController(), LookController(true, true)),
            SimpleFlatAStarRouteFinder(WalkingPosEvaluator(), this),
            this
        )
    }
}
