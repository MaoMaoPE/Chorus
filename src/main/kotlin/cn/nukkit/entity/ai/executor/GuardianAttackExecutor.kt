package cn.nukkit.entity.ai.executor

import cn.nukkit.Player
import cn.nukkit.Server
import cn.nukkit.entity.*
import cn.nukkit.entity.ai.memory.MemoryType
import cn.nukkit.entity.data.EntityDataTypes
import cn.nukkit.entity.mob.EntityMob
import cn.nukkit.entity.mob.monster.EntityMonster
import cn.nukkit.event.entity.EntityDamageByEntityEvent
import cn.nukkit.event.entity.EntityDamageEvent.DamageCause
import cn.nukkit.network.protocol.EntityEventPacket
import cn.nukkit.network.protocol.LevelSoundEventPacketV2

class GuardianAttackExecutor(
    protected var memory: MemoryType<out Entity?>,
    protected var speed: Float,
    maxShootDistance: Int,
    protected var clearDataWhenLose: Boolean,
    protected val coolDownTick: Int,
    protected val attackDelay: Int
) :
    EntityControl, IBehaviorExecutor {
    protected var maxShootDistanceSquared: Int = maxShootDistance * maxShootDistance

    /**
     *
     *
     * Used to specify a specific attack target.
     */
    protected var target: Entity? = null

    private var tick1 = 0 //control the coolDownTick
    private var tick2 = 0 //control the pullBowTick

    override fun execute(entity: EntityMob): Boolean {
        if (tick2 == 0) {
            tick1++
        }
        if (!entity.isEnablePitch) entity.isEnablePitch = true
        if (entity.behaviorGroup!!.memoryStorage!!.isEmpty(memory)) return false
        val newTarget = entity.behaviorGroup!!.memoryStorage!![memory]
        if (this.target == null) target = newTarget

        if (!target!!.isAlive) return false
        else if (target is Player) {
            if (target.isCreative() || target.isSpectator() || !target.isOnline() || (entity.level!!.name != target.level.name)) {
                return false
            }
        }

        if (target!!.locator != newTarget!!.locator) {
            target = newTarget
        }

        if (entity.movementSpeed != speed) entity.movementSpeed = speed
        val clone = target!!.transform

        setLookTarget(entity, clone.position)

        if (tick2 == 0 && tick1 > coolDownTick) {
            if (entity.position.distanceSquared(target!!.position) <= maxShootDistanceSquared) {
                this.tick1 = 0
                tick2++
                startSequence(entity)
            }
        } else if (tick2 != 0) {
            tick2++
            if (tick2 > attackDelay) {
                if (entity.getDataProperty<Long>(EntityDataTypes.Companion.TARGET_EID!!, 0L) == target!!.id) {
                    val event = EntityDamageByEntityEvent(
                        entity,
                        target!!,
                        DamageCause.ENTITY_ATTACK,
                        (entity as EntityMonster).getDiffHandDamage(Server.getInstance().difficulty)
                    )
                    target!!.attack(event)
                    if (Server.getInstance().difficulty >= 2) {
                        val event2 = EntityDamageByEntityEvent(
                            entity,
                            target!!, DamageCause.MAGIC, 1f
                        )
                        target!!.attack(event2)
                    }
                }
                endSequence(entity)
                tick2 = 0
                return target!!.health != 0f
            }
        }
        return true
    }

    override fun onStop(entity: EntityMob) {
        removeLookTarget(entity)
        entity.movementSpeed = EntityLiving.Companion.DEFAULT_SPEED
        if (clearDataWhenLose) {
            entity.behaviorGroup!!.memoryStorage!!.clear(memory)
        }
        entity.isEnablePitch = false
        endSequence(entity)
        this.target = null
    }

    override fun onInterrupt(entity: EntityMob) {
        removeLookTarget(entity)
        entity.movementSpeed = EntityLiving.Companion.DEFAULT_SPEED
        if (clearDataWhenLose) {
            entity.behaviorGroup!!.memoryStorage!!.clear(memory)
        }
        entity.isEnablePitch = false
        endSequence(entity)
        this.target = null
    }

    private fun startSequence(entity: Entity) {
        entity.setDataProperty(EntityDataTypes.Companion.TARGET_EID, target!!.id)
        entity.level!!.addLevelSoundEvent(
            entity.position,
            LevelSoundEventPacketV2.SOUND_MOB_WARNING,
            -1,
            entity.getIdentifier(),
            false,
            false
        )
    }

    private fun endSequence(entity: Entity) {
        entity.setDataProperty(EntityDataTypes.Companion.TARGET_EID, 0L)
        val pk = EntityEventPacket()
        pk.event = EntityEventPacket.GUARDIAN_ATTACK_ANIMATION
        pk.eid = entity.id
        pk.data = 0
        Server.broadcastPacket(entity.viewers.values, pk)
    }
}
