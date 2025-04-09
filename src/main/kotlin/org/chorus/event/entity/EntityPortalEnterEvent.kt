package org.chorus.event.entity

import org.chorus.entity.Entity
import org.chorus.event.Cancellable
import org.chorus.event.HandlerList

class EntityPortalEnterEvent(entity: Entity, type: PortalType) : EntityEvent(), Cancellable {
    val portalType: PortalType

    init {
        this.entity = entity
        this.portalType = type
    }

    enum class PortalType {
        NETHER,
        END
    }

    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
