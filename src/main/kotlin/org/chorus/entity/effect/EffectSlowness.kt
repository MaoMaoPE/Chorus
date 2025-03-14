package org.chorus.entity.effect

import org.chorus.entity.Entity
import org.chorus.entity.EntityLiving
import java.awt.Color

class EffectSlowness :
    Effect(EffectType.SLOWNESS, "%potion.moveSlowdown", Color(139, 175, 224), true) {
    override fun add(entity: Entity) {
        if (entity is EntityLiving) {
            val oldEffect: Effect? = entity.getEffect(this.getType())
            if (oldEffect != null) {
                entity.setMovementSpeed(entity.getMovementSpeed() / (1 - 0.15f * oldEffect.getLevel()))
            }

            entity.setMovementSpeed(entity.getMovementSpeed() * (1 - 0.15f * this.getLevel()))
        }
    }

    override fun remove(entity: Entity) {
        if (entity is EntityLiving) {
            entity.setMovementSpeed(entity.getMovementSpeed() / (1 - 0.15f * this.getLevel()))
        }
    }
}
