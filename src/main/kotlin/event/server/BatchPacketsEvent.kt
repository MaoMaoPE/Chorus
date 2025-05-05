package org.chorus_oss.chorus.event.server

import org.chorus_oss.chorus.Player
import org.chorus_oss.chorus.event.Cancellable
import org.chorus_oss.chorus.event.HandlerList
import org.chorus_oss.chorus.network.protocol.DataPacket

class BatchPacketsEvent(val players: Array<Player>, val packets: Array<DataPacket>, val isForceSync: Boolean) :
    ServerEvent(), Cancellable {
    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
