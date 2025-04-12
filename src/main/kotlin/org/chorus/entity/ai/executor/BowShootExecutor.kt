package org.chorus.entity.ai.executor

import org.chorus.Player
import org.chorus.Server
import org.chorus.entity.Entity
import org.chorus.entity.EntityID
import org.chorus.entity.EntityLiving
import org.chorus.entity.ai.memory.NullableMemoryType
import org.chorus.entity.data.EntityDataTypes
import org.chorus.entity.data.EntityFlag
import org.chorus.entity.mob.EntityMob
import org.chorus.entity.projectile.EntityProjectile
import org.chorus.entity.projectile.abstract_arrow.EntityArrow
import org.chorus.event.entity.EntityShootBowEvent
import org.chorus.event.entity.ProjectileLaunchEvent
import org.chorus.item.Item
import org.chorus.item.ItemBow
import org.chorus.item.enchantment.Enchantment
import org.chorus.item.enchantment.bow.EnchantmentBow
import org.chorus.level.Sound
import org.chorus.nbt.tag.CompoundTag
import org.chorus.nbt.tag.FloatTag
import org.chorus.nbt.tag.ListTag
import java.util.function.Supplier
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

class BowShootExecutor(
    /**
     * 用来射击的物品
     */
    protected var item: Supplier<Item>,
    protected var memory: NullableMemoryType<out Entity>,
    protected var speed: Float,
    maxShootDistance: Int,
    protected var clearDataWhenLose: Boolean,
    protected val coolDownTick: Int,
    protected val pullBowTick: Int
) :
    EntityControl, IBehaviorExecutor {
    protected var maxShootDistanceSquared: Int = maxShootDistance * maxShootDistance

    /**
     * 用来指定特定的攻击目标.
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
        if (!entity.isEnablePitch) entity.isEnablePitch = (true)
        if (entity.behaviorGroup.memoryStorage.isEmpty(memory)) return false
        val newTarget = entity.behaviorGroup.memoryStorage[memory]
        if (this.target == null) target = newTarget
        //some check
        if (!target!!.isAlive()) return false
        else if (target is Player) {
            val player = target as Player
            if (player.isCreative || player.isSpectator || !player.isOnline || (entity.level!!.name != player.level!!.name)) {
                return false
            }
        }

        if (target!!.locator != newTarget!!.locator) {
            //更新目标
            target = newTarget
        }

        if (entity.movementSpeed != speed) entity.setMovementSpeedF(speed)
        val clone = target!!.transform

        if (entity.position.distanceSquared(target!!.position) > maxShootDistanceSquared) {
            //更新寻路target
            setRouteTarget(entity, clone.position)
        } else {
            setRouteTarget(entity, null)
        }
        //更新视线target
        setLookTarget(entity, clone.position)

        if (tick2 == 0 && tick1 > coolDownTick) {
            if (entity.position.distanceSquared(target!!.position) <= maxShootDistanceSquared) {
                this.tick1 = 0
                tick2++
                playBowAnimation(entity)
            }
        } else if (tick2 != 0) {
            tick2++
            if (tick2 > pullBowTick) {
                val tool = item.get()
                if (tool is ItemBow) {
                    bowShoot(tool, entity)
                    stopBowAnimation(entity)
                    tick2 = 0
                    return target!!.health != 0f
                }
            }
        }
        return true
    }

    override fun onStop(entity: EntityMob) {
        removeRouteTarget(entity)
        removeLookTarget(entity)
        //重置速度
        entity.setMovementSpeedF(EntityLiving.DEFAULT_SPEED)
        if (clearDataWhenLose) {
            entity.behaviorGroup.memoryStorage.clear(memory)
        }
        entity.isEnablePitch = (false)
        stopBowAnimation(entity)
        this.target = null
    }

    override fun onInterrupt(entity: EntityMob) {
        removeRouteTarget(entity)
        removeLookTarget(entity)
        //重置速度
        entity.setMovementSpeedF(EntityLiving.DEFAULT_SPEED)
        if (clearDataWhenLose) {
            entity.behaviorGroup.memoryStorage.clear(memory)
        }
        entity.isEnablePitch = (false)
        stopBowAnimation(entity)
        this.target = null
    }

    protected fun bowShoot(bow: ItemBow, entity: EntityMob) {
        playBowAnimation(entity)
        var damage = 2.0
        val bowDamage = bow.getEnchantment(Enchantment.ID_BOW_POWER)
        if (bowDamage != null && bowDamage.level > 0) {
            damage += bowDamage.level.toDouble() * 0.5 + 0.5
        }
        val flameEnchant = bow.getEnchantment(Enchantment.ID_BOW_FLAME)
        val flame = flameEnchant != null && flameEnchant.level > 0

        val nbt = CompoundTag()
            .putList(
                "Pos", ListTag<FloatTag>()
                    .add(FloatTag(entity.position.x))
                    .add(FloatTag(entity.position.y + entity.getCurrentHeight() / 2 + 0.2f))
                    .add(FloatTag(entity.position.z))
            )
            .putList(
                "Motion", ListTag<FloatTag>()
                    .add(FloatTag(-sin(entity.headYaw / 180 * Math.PI) * cos(entity.rotation.pitch / 180 * Math.PI)))
                    .add(FloatTag(-sin(entity.rotation.pitch / 180 * Math.PI)))
                    .add(FloatTag(cos(entity.headYaw / 180 * Math.PI) * cos(entity.rotation.pitch / 180 * Math.PI)))
            )
            .putList(
                "Rotation", ListTag<FloatTag>()
                    .add(FloatTag((if (entity.headYaw > 180) 360 else 0).toFloat() - entity.headYaw))
                    .add(FloatTag(-entity.rotation.pitch.toFloat()))
            )
            .putShort("Fire", if (flame) 45 * 60 else 0)
            .putDouble("damage", damage)

        val p = pullBowTick.toDouble() / 20
        val f = min((p * p + p * 2) / 3, 1.0) * 3

        val arrow = Entity.createEntity(
            EntityID.ARROW,
            entity.chunk!!, nbt, entity, f == 2.0
        ) as EntityArrow?

        if (arrow == null) {
            return
        }

        val entityShootBowEvent = EntityShootBowEvent(entity, bow, arrow, f)
        Server.instance.pluginManager.callEvent(entityShootBowEvent)
        if (entityShootBowEvent.isCancelled) {
            entityShootBowEvent.getProjectile().kill()
        } else {
            entityShootBowEvent.getProjectile().setMotion(
                entityShootBowEvent.getProjectile().getMotion().multiply(entityShootBowEvent.force)
            )
            val infinityEnchant = bow.getEnchantment(Enchantment.ID_BOW_INFINITY)
            val infinity = infinityEnchant != null && infinityEnchant.level > 0
            val projectile: EntityProjectile = entityShootBowEvent.getProjectile()
            if (infinity && projectile is EntityArrow) {
                projectile.setPickupMode(EntityProjectile.Companion.PICKUP_CREATIVE)
            }

            for (enc in bow.enchantments) {
                if (enc is EnchantmentBow) {
                    enc.onBowShoot(entity, arrow, bow)
                }
            }

            if (entityShootBowEvent.getProjectile() != null) {
                val projectev = ProjectileLaunchEvent(entityShootBowEvent.getProjectile(), entity)
                Server.instance.pluginManager.callEvent(projectev)
                if (projectev.isCancelled) {
                    entityShootBowEvent.getProjectile().kill()
                } else {
                    entityShootBowEvent.getProjectile().spawnToAll()
                    entity.level!!.addSound(entity.position, Sound.RANDOM_BOW)
                }
            }
        }
    }

    private fun playBowAnimation(entity: Entity) {
        entity.setDataProperty(EntityDataTypes.TARGET_EID, target!!.getRuntimeID())
        entity.setDataFlag(EntityFlag.FACING_TARGET_TO_RANGE_ATTACK)
    }

    private fun stopBowAnimation(entity: Entity) {
        entity.setDataProperty(EntityDataTypes.TARGET_EID, 0L)
        entity.setDataFlag(EntityFlag.FACING_TARGET_TO_RANGE_ATTACK, false)
    }
}
