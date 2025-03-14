package org.chorus.entity.effect

import org.chorus.entity.Entity
import org.chorus.entity.data.EntityFlag
import java.awt.Color

class EffectInvisibility : Effect(EffectType.INVISIBILITY, "%potion.invisibility", Color(246, 246, 246)) {
    override fun add(entity: Entity) {
        entity.setDataFlag(EntityFlag.INVISIBLE, true)
        entity.setNameTagVisible(false)
    }

    override fun remove(entity: Entity) {
        entity.setDataFlag(EntityFlag.INVISIBLE, false)
        entity.setNameTagVisible(true)
    }
}
