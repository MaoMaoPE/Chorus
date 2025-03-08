package cn.nukkit.entity.ai.executor

import cn.nukkit.Player
import cn.nukkit.block.*
import cn.nukkit.entity.ai.memory.CoreMemoryTypes
import cn.nukkit.entity.mob.EntityMob
import cn.nukkit.entity.mob.animal.EntityHorse
import cn.nukkit.network.protocol.EntityEventPacket
import cn.nukkit.utils.*
import com.google.common.base.Preconditions

/**
 * 代表玩家驯服马时，马的行为
 *
 *
 * Represents the behavior of a horse when the player tames it
 */
class TameHorseExecutor @JvmOverloads constructor(
    speed: Float,
    maxRoamRange: Int,
    frequency: Int,
    calNextTargetImmediately: Boolean = false,
    runningTime: Int = 100,
    avoidWater: Boolean = false,
    maxRetryTime: Int = 10,
    tameProbability: Int = 35
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
    protected val tameProbability: Int
    private var tick1: Int //control the stopTameFailAnimation

    /**
     * Instantiates a new Flat random roam executor.
     *
     * @param speed                    移动速度<br></br>Movement speed
     * @param maxRoamRange             随机行走目标点的范围<br></br>The range of the target point that is randomly walked
     * @param frequency                更新目标点的频率<br></br>How often the target point is updated
     * @param calNextTargetImmediately 是否立即选择下一个目标点,不管执行频率<br></br>Whether to select the next target point immediately, regardless of the frequency of execution
     * @param runningTime              马儿随机跑动的时间，跑动结束后会判断是否驯服成功<br></br>The time when the horse runs randomly, after the run, will judge whether the taming is successful
     * @param avoidWater               是否避开水行走<br></br>Whether to walk away from water
     * @param maxRetryTime             选取目标点的最大尝试次数<br></br>Pick the maximum number of attempts at the target point
     * @param tameProbability          马被驯服的概率(取值范围1-100)<br></br>Probability of a horse being tamed (value range 1-100)
     */
    init {
        Preconditions.checkArgument(tameProbability > 0 && tameProbability <= 100)
        this.tameProbability = tameProbability
        this.tick1 = 0
    }

    override fun execute(entity: EntityMob): Boolean {
        //Fail Animation
        if (tick1 != 0) {
            if (tick1 > 13) {
                val horse = entity as EntityHorse
                horse.stopTameFailAnimation()
                return false
            }
            tick1++
            return true
        }

        currentTargetCalTick++
        durationTick++
        if (entity.isEnablePitch) entity.isEnablePitch = false
        if (currentTargetCalTick >= frequency || (calNextTargetImmediately && needUpdateTarget(entity))) {
            var target = next(entity)
            if (avoidWater) {
                var blockId: String
                var time = 0
                while (time <= maxRetryTime && ((entity.level!!.getTickCachedBlock(
                        target!!.add(
                            0.0,
                            -1.0,
                            0.0
                        )
                    ).id.also { blockId = it }) === Block.FLOWING_WATER || blockId === Block.WATER)
                ) {
                    target = next(entity)
                    time++
                }
            }
            if (entity.movementSpeed != speed) entity.movementSpeed = speed
            //更新寻路target
            setRouteTarget(entity, target)
            //更新视线target
            setLookTarget(entity, target)
            currentTargetCalTick = 0
            entity.behaviorGroup!!.isForceUpdateRoute = calNextTargetImmediately
        }
        if (durationTick <= runningTime || runningTime == -1) return true
        else {
            currentTargetCalTick = 0
            durationTick = 0
            if (Utils.rand(0, 100) <= tameProbability) {
                val horse = entity as EntityHorse
                horse.ownerName = horse.memoryStorage.get<String>(CoreMemoryTypes.Companion.RIDER_NAME)
                val packet = EntityEventPacket()
                packet.eid = horse.id
                packet.event = EntityEventPacket.TAME_SUCCESS
                val player = horse.rider as Player? ?: return false
                player.dataPacket(packet)
            } else {
                val horse = entity as EntityHorse
                val packet = EntityEventPacket()
                packet.eid = horse.id
                packet.event = EntityEventPacket.TAME_FAIL
                val player = horse.rider as Player? ?: return false
                player.dataPacket(packet)
                horse.playTameFailAnimation()
                horse.dismountEntity(horse.rider!!)
                tick1++
                return true
            }
            return false
        }
    }

    override fun onStop(entity: EntityMob?) {
        super.onStop(entity)
        tick1 = 0
    }

    override fun onStart(entity: EntityMob?) {
        super.onStart(entity!!)
        tick1 = 0
    }
}
