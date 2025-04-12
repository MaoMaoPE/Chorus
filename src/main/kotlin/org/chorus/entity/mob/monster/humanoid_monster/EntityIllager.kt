package org.chorus.entity.mob.monster.humanoid_monster

import org.chorus.entity.Entity
import org.chorus.entity.EntityID
import org.chorus.entity.EntityWalkable
import org.chorus.entity.mob.villagers.EntityVillager
import org.chorus.level.format.IChunk
import org.chorus.nbt.tag.CompoundTag

abstract class EntityIllager(chunk: IChunk?, nbt: CompoundTag?) : EntityHumanoidMonster(chunk, nbt), EntityWalkable {
    override fun attackTarget(entity: Entity): Boolean {
        return when (entity.getEntityIdentifier()) {
            EntityID.VILLAGER -> entity is EntityVillager && !entity.isBaby()
            EntityID.IRON_GOLEM, EntityID.WANDERING_TRADER -> true
            else -> super.attackTarget(entity)
        }
    }
}
