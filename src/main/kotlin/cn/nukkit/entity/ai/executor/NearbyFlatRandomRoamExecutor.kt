package cn.nukkit.entity.ai.executor

import cn.nukkit.block.Block
import cn.nukkit.entity.ai.memory.MemoryType
import cn.nukkit.entity.mob.EntityMob
import cn.nukkit.math.IVector3
import cn.nukkit.math.Vector3
import java.util.concurrent.ThreadLocalRandom

open class NearbyFlatRandomRoamExecutor @JvmOverloads constructor(
    protected var memory: MemoryType<out IVector3?>,
    speed: Float,
    maxRoamRange: Int,
    frequency: Int,
    calNextTargetImmediately: Boolean = false,
    runningTime: Int = 100,
    avoidWater: Boolean = false,
    maxRetryTime: Int = 10
) :
    FlatRandomRoamExecutor(
        speed,
        maxRoamRange,
        frequency,
        calNextTargetImmediately,
        runningTime,
        avoidWater,
        maxRetryTime
    ) {
    override fun execute(entity: EntityMob): Boolean {
        currentTargetCalTick++
        durationTick++

        val center = entity.memoryStorage!![memory].vector3 ?: return false

        if (entity.isEnablePitch) entity.isEnablePitch = false
        if (currentTargetCalTick >= frequency || (calNextTargetImmediately && needUpdateTarget(entity))) {
            var target = next(center)
            if (avoidWater) {
                var blockId: String
                var time = 0
                while (time <= maxRetryTime && ((entity.level!!.getTickCachedBlock(
                        target.add(
                            0.0,
                            -1.0,
                            0.0
                        )
                    ).id.also { blockId = it }) === Block.FLOWING_WATER || blockId === Block.WATER)
                ) {
                    target = next(center)
                    time++
                }
            }
            if (entity.movementSpeed != speed) entity.movementSpeed = speed

            setRouteTarget(entity, target)
            setLookTarget(entity, target)
            currentTargetCalTick = 0
            entity.behaviorGroup!!.isForceUpdateRoute = calNextTargetImmediately
        }
        if (durationTick <= runningTime || runningTime == -1) return true
        else {
            currentTargetCalTick = 0
            durationTick = 0
            return false
        }
    }

    protected fun next(entity: Vector3): Vector3 {
        val random = ThreadLocalRandom.current()
        val x = random.nextInt(maxRoamRange * 2) - maxRoamRange + entity.floorX
        val z = random.nextInt(maxRoamRange * 2) - maxRoamRange + entity.floorZ
        return Vector3(x.toDouble(), entity.y, z.toDouble())
    }
}
