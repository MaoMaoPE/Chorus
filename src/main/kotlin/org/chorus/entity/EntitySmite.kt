package org.chorus.entity

import org.chorus.entity.effect.*
import org.chorus.inventory.EntityInventoryHolder
import org.chorus.level.*

/**
 * 这个接口代表亡灵类的怪物实体
 *
 *
 * This interface represents the monster entity of the undead class
 */
interface EntitySmite {
    fun burn(entity: Entity) {
        if (entity.level!!.dimension == Level.DIMENSION_OVERWORLD && entity.level!!.isDaytime && !entity.level!!.isRaining && (!entity.hasEffect(
                EffectType.FIRE_RESISTANCE
            ) || (entity is EntityInventoryHolder && entity.helmet.isNothing))
            && !entity.isInsideOfWater() && !entity.isUnderBlock() && !entity.isOnFire()
        ) {
            entity.setOnFire(1)
        }
    }
}
