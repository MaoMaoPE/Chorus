package org.chorus.entity.mob.animal

import org.chorus.entity.EntityID
import org.chorus.entity.EntityWalkable
import org.chorus.entity.ai.behavior.Behavior
import org.chorus.entity.ai.behavior.IBehavior
import org.chorus.entity.ai.behaviorgroup.BehaviorGroup
import org.chorus.entity.ai.behaviorgroup.IBehaviorGroup
import org.chorus.entity.ai.controller.FluctuateController
import org.chorus.entity.ai.controller.IController
import org.chorus.entity.ai.controller.LookController
import org.chorus.entity.ai.controller.WalkController
import org.chorus.entity.ai.evaluator.IBehaviorEvaluator
import org.chorus.entity.ai.evaluator.MemoryCheckNotEmptyEvaluator
import org.chorus.entity.ai.evaluator.PassByTimeEvaluator
import org.chorus.entity.ai.evaluator.ProbabilityEvaluator
import org.chorus.entity.ai.executor.*
import org.chorus.entity.ai.memory.CoreMemoryTypes
import org.chorus.entity.ai.route.finder.impl.SimpleFlatAStarRouteFinder
import org.chorus.entity.ai.route.posevaluator.WalkingPosEvaluator
import org.chorus.entity.ai.sensor.ISensor
import org.chorus.entity.ai.sensor.NearestFeedingPlayerSensor
import org.chorus.entity.ai.sensor.NearestPlayerSensor
import org.chorus.entity.mob.EntityMob
import org.chorus.item.Item
import org.chorus.item.ItemID
import org.chorus.level.format.IChunk
import org.chorus.nbt.tag.CompoundTag

class EntityFox(chunk: IChunk?, nbt: CompoundTag) : EntityAnimal(chunk, nbt), EntityWalkable {
    override fun getIdentifier(): String {
        return EntityID.FOX
    }


    public override fun requireBehaviorGroup(): IBehaviorGroup {
        return BehaviorGroup(
            this.tickSpread,
            setOf<IBehavior>( //用于刷新InLove状态的核心行为
                Behavior(
                    InLoveExecutor(400),
                    all(
                        PassByTimeEvaluator(CoreMemoryTypes.Companion.LAST_BE_FEED_TIME, 0, 400),
                        PassByTimeEvaluator(CoreMemoryTypes.Companion.LAST_IN_LOVE_TIME, 6000, Int.MAX_VALUE)
                    ),
                    1, 1
                )
            ),
            setOf<IBehavior>(
                Behavior(
                    FlatRandomRoamExecutor(0.4f, 12, 40, true, 100, true, 10),
                    PassByTimeEvaluator(CoreMemoryTypes.Companion.LAST_BE_ATTACKED_TIME, 0, 100),
                    4,
                    1
                ),
                Behavior(
                    EntityBreedingExecutor<EntityFox>(EntityFox::class.java, 16, 100, 0.5f),
                    { entity: EntityMob -> entity.memoryStorage.get<Boolean>(CoreMemoryTypes.Companion.IS_IN_LOVE) },
                    3,
                    1
                ),
                Behavior(
                    MoveToTargetExecutor(CoreMemoryTypes.Companion.NEAREST_FEEDING_PLAYER, 0.4f, true),
                    MemoryCheckNotEmptyEvaluator(CoreMemoryTypes.Companion.NEAREST_FEEDING_PLAYER),
                    2,
                    1
                ),
                Behavior(
                    LookAtTargetExecutor(CoreMemoryTypes.Companion.NEAREST_PLAYER, 100),
                    ProbabilityEvaluator(4, 10),
                    1,
                    1,
                    100
                ),
                Behavior(
                    FlatRandomRoamExecutor(0.2f, 12, 100, false, -1, true, 10),
                    (IBehaviorEvaluator { entity: EntityMob? -> true }),
                    1,
                    1
                )
            ),
            setOf<ISensor>(NearestFeedingPlayerSensor(8.0, 0.0), NearestPlayerSensor(8.0, 0.0, 20)),
            setOf<IController>(WalkController(), LookController(true, true), FluctuateController()),
            SimpleFlatAStarRouteFinder(WalkingPosEvaluator(), this),
            this
        )
    }

    override fun getWidth(): Float {
        return 0.6f
    }

    override fun getHeight(): Float {
        return 0.7f
    }

    override fun initEntity() {
        this.maxHealth = 20
        super.initEntity()
    }

    override fun getOriginalName(): String {
        return "Fox"
    }

    override fun isBreedingItem(item: Item): Boolean {
        return item.id == ItemID.GLOW_BERRIES || item.id == ItemID.SWEET_BERRIES
    }
}
