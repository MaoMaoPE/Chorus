package org.chorus.entity.projectile.abstract_arrow

import org.chorus.Server
import org.chorus.entity.Entity
import org.chorus.entity.EntityID
import org.chorus.entity.data.EntityDataTypes
import org.chorus.entity.data.EntityFlag
import org.chorus.entity.projectile.EntityProjectile
import org.chorus.item.ItemArrow
import org.chorus.level.Sound
import org.chorus.level.format.IChunk
import org.chorus.nbt.tag.CompoundTag
import org.chorus.network.protocol.EntityEventPacket
import java.util.concurrent.ThreadLocalRandom
import java.util.function.Consumer


class EntityArrow @JvmOverloads constructor(
    chunk: IChunk?,
    nbt: CompoundTag,
    shootingEntity: Entity? = null,
    critical: Boolean = false
) :
    EntityAbstractArrow(chunk, nbt, shootingEntity) {
    override fun getIdentifier(): String {
        return EntityID.ARROW
    }

    var pickupMode: Int = 0

    protected var item: ItemArrow? = null

    init {
        this.setCritical(critical)
    }

    override fun getLength(): Float {
        return 0.5f
    }

    override fun getGravity(): Float {
        return 0.05f
    }

    public override fun getDrag(): Float {
        return 0.01f
    }

    override fun updateMotion() {
        if (!isInsideOfWater()) {
            super.updateMotion()
            return
        }

        val drag: Float = 1 - this.getDrag() * 20

        motion.y -= (getGravity() * 2).toDouble()
        if (motion.y < 0) {
            motion.y *= drag / 1.5
        }
        motion.x *= drag.toDouble()
        motion.z *= drag.toDouble()
    }

    override fun initEntity() {
        super.initEntity()

        this.pickupMode =
            (if (namedTag!!.contains("pickup")) namedTag!!.getByte("pickup") else EntityProjectile.Companion.PICKUP_ANY.toByte()).toInt()
    }

    fun setCritical() {
        this.setCritical(true)
    }

    fun isCritical(): Boolean {
        return this.getDataFlag(EntityFlag.CRITICAL)
    }

    fun setCritical(value: Boolean) {
        this.setDataFlag(EntityFlag.CRITICAL, value)
    }

    override fun getResultDamage(): Int {
        var base: Int = super.getResultDamage()

        if (this.isCritical()) {
            base += ThreadLocalRandom.current().nextInt(base / 2 + 2)
        }

        return base
    }

    override fun getBaseDamage(): Double {
        return 2.0
    }

    override fun onUpdate(currentTick: Int): Boolean {
        if (this.closed) {
            return false
        }

        var hasUpdate: Boolean = super.onUpdate(currentTick)

        if (this.onGround || this.hadCollision) {
            this.setCritical(false)
        }

        if (this.age > 1200) {
            this.close()
            hasUpdate = true
        }

        if (level!!.isRaining && this.fireTicks > 0 && level!!.canBlockSeeSky(this.position)) {
            extinguish()

            hasUpdate = true
        }

        return hasUpdate
    }

    override fun canBeMovedByCurrents(): Boolean {
        return !hadCollision
    }

    override fun afterCollisionWithEntity(entity: Entity) {
        if (hadCollision) {
            if (getArrowItem() != null) {
                if (getArrowItem()!!.tippedArrowPotion != null) {
                    getArrowItem()!!.tippedArrowPotion!!
                        .getEffects(false).forEach(Consumer { entity.addEffect(it) })
                }
            }
            close()
        } else {
            setMotion(getMotion().divide(-4.0))
        }
    }

    override fun addHitEffect() {
        level!!.addSound(this.position, Sound.RANDOM_BOWHIT)
        val packet: EntityEventPacket = EntityEventPacket()
        packet.eid = getRuntimeID()
        packet.event = EntityEventPacket.ARROW_SHAKE
        packet.data = 7 // TODO Magic value. I have no idea why we have to set it to 7 here...
        Server.broadcastPacket(hasSpawned.values, packet)
        onGround = true
    }

    override fun saveNBT() {
        super.saveNBT()

        namedTag!!.putByte("pickup", this.pickupMode)
    }

    fun getPickupMode(): Int {
        return this.pickupMode
    }

    fun setPickupMode(pickupMode: Int) {
        this.pickupMode = pickupMode
    }

    fun setItem(arrow: ItemArrow) {
        this.item = arrow
        if (arrow.tippedArrowPotion != null) {
            this.setDataProperty(EntityDataTypes.Companion.CUSTOM_DISPLAY, arrow.tippedArrowPotion!!.id + 1)
        }
    }

    fun getArrowItem(): ItemArrow? {
        return this.item
    }

    override fun getOriginalName(): String {
        return "Arrow"
    }
}
