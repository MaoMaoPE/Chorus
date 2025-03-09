package org.chorus.event.vehicle

import cn.nukkit.entity.item.EntityVehicle
import cn.nukkit.event.Cancellable
import cn.nukkit.event.HandlerList

/**
 * Is called when an entity takes damage
 */
open class VehicleDamageEvent
/**
 * Constructor for the VehicleDamageEvent
 *
 * @param vehicle the damaged vehicle
 * @param damage  the caused damage on the vehicle
 */(
    vehicle: EntityVehicle,
    /**
     * Sets the damage caused on the vehicle
     *
     * @param damage the caused damage
     */
    var damage: Double
) : VehicleEvent(vehicle), Cancellable {
    /**
     * Returns the caused damage on the vehicle
     *
     * @return caused damage on the vehicle
     */

    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
