package org.chorus_oss.chorus.entity.mob.animal

import org.chorus_oss.chorus.Player
import org.chorus_oss.chorus.entity.EntityID
import org.chorus_oss.chorus.entity.EntityWalkable
import org.chorus_oss.chorus.entity.ai.behavior.Behavior
import org.chorus_oss.chorus.entity.ai.behavior.IBehavior
import org.chorus_oss.chorus.entity.ai.behaviorgroup.BehaviorGroup
import org.chorus_oss.chorus.entity.ai.behaviorgroup.IBehaviorGroup
import org.chorus_oss.chorus.entity.ai.controller.FluctuateController
import org.chorus_oss.chorus.entity.ai.controller.IController
import org.chorus_oss.chorus.entity.ai.controller.LookController
import org.chorus_oss.chorus.entity.ai.controller.WalkController
import org.chorus_oss.chorus.entity.ai.evaluator.IBehaviorEvaluator
import org.chorus_oss.chorus.entity.ai.evaluator.MemoryCheckNotEmptyEvaluator
import org.chorus_oss.chorus.entity.ai.evaluator.PassByTimeEvaluator
import org.chorus_oss.chorus.entity.ai.evaluator.ProbabilityEvaluator
import org.chorus_oss.chorus.entity.ai.executor.*
import org.chorus_oss.chorus.entity.ai.memory.CoreMemoryTypes
import org.chorus_oss.chorus.entity.ai.route.finder.impl.SimpleFlatAStarRouteFinder
import org.chorus_oss.chorus.entity.ai.route.posevaluator.WalkingPosEvaluator
import org.chorus_oss.chorus.entity.ai.sensor.ISensor
import org.chorus_oss.chorus.entity.ai.sensor.NearestFeedingPlayerSensor
import org.chorus_oss.chorus.entity.ai.sensor.NearestPlayerSensor
import org.chorus_oss.chorus.entity.mob.EntityMob
import org.chorus_oss.chorus.item.Item
import org.chorus_oss.chorus.item.ItemID
import org.chorus_oss.chorus.level.format.IChunk
import org.chorus_oss.chorus.math.Vector3
import org.chorus_oss.chorus.nbt.tag.CompoundTag
import org.chorus_oss.chorus.utils.Utils

class EntityCow(chunk: IChunk?, nbt: CompoundTag?) : EntityAnimal(chunk, nbt!!), EntityWalkable {
    override fun getEntityIdentifier(): String {
        return EntityID.COW
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
                    EntityBreedingExecutor<EntityCow>(EntityCow::class.java, 16, 100, 0.5f),
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
        if (this.isBaby()) {
            return 0.45f
        }
        return 0.9f
    }

    override fun getHeight(): Float {
        if (this.isBaby()) {
            return 0.65f
        }
        return 1.3f
    }

    override fun getOriginalName(): String {
        return "Cow"
    }

    override fun getDrops(): Array<Item> {
        if (!this.isBaby()) {
            return arrayOf(
                Item.get(ItemID.LEATHER, 0, Utils.rand(0, 2)),
                Item.get((if (this.isOnFire()) ItemID.COOKED_BEEF else ItemID.BEEF), 0, Utils.rand(1, 3))
            )
        }
        return Item.EMPTY_ARRAY
    }


    override fun initEntity() {
        this.maxHealth = 10
        super.initEntity()
    }

    override fun onInteract(player: Player, item: Item, clickedPos: Vector3): Boolean {
        if (super.onInteract(player, item, clickedPos)) {
            return true
        }

        if (item.id === ItemID.BUCKET && item.damage == 0) {
            item.count--
            player.inventory.addItem(Item.get(ItemID.BUCKET, 1))
            return true
        }

        return false
    }
}
