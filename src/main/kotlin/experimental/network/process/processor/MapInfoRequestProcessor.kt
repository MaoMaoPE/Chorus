package org.chorus_oss.chorus.experimental.network.process.processor

import org.chorus_oss.chorus.Player
import org.chorus_oss.chorus.Server
import org.chorus_oss.chorus.blockentity.BlockEntityItemFrame
import org.chorus_oss.chorus.event.player.PlayerMapInfoRequestEvent
import org.chorus_oss.chorus.item.Item
import org.chorus_oss.chorus.item.ItemFilledMap
import org.chorus_oss.chorus.network.process.DataPacketProcessor
import org.chorus_oss.chorus.network.protocol.MapInfoRequestPacket
import org.chorus_oss.chorus.network.ProtocolInfo
import org.chorus_oss.chorus.plugin.InternalPlugin
import org.chorus_oss.chorus.scheduler.AsyncTask


class MapInfoRequestProcessor : DataPacketProcessor<MapInfoRequestPacket>() {
    override fun handle(player: Player, pk: MapInfoRequestPacket) {
        val player = player.player
        var mapItem: Item? = null
        var index = 0
        var offhand = false

        for ((key, item1) in player.offhandInventory.contents) {
            if (checkMapItemValid(item1, pk)) {
                mapItem = item1
                index = key
                offhand = true
            }
        }

        if (mapItem == null) {
            for ((key, item1) in player.inventory.contents) {
                if (checkMapItemValid(item1, pk)) {
                    mapItem = item1
                    index = key
                }
            }
        }

        if (mapItem == null) {
            for (be in player.level!!.getBlockEntities().values) {
                if (be is BlockEntityItemFrame && checkMapItemValid(be.item, pk)) {
                    (be.item as ItemFilledMap).sendImage(player)
                    break
                }
            }
        }

        if (mapItem != null) {
            val event: PlayerMapInfoRequestEvent
            Server.instance.pluginManager.callEvent(PlayerMapInfoRequestEvent(player, mapItem).also { event = it })

            if (!event.cancelled) {
                val map = mapItem as ItemFilledMap
                if (map.trySendImage(player)) {
                    return
                }

                val finalIndex = index
                val finalOffhand = offhand
                //TODO: 并行计算
                player.level!!.scheduler.scheduleAsyncTask(InternalPlugin.INSTANCE, object : AsyncTask() {
                    override fun onRun() {
                        map.renderMap(
                            player.level!!,
                            (player.position.floorX / 128) shl 7,
                            (player.position.floorZ / 128) shl 7,
                            1
                        )
                        if (finalOffhand) {
                            if (checkMapItemValid(
                                    player.offhandInventory.getUnclonedItem(finalIndex),
                                    pk
                                )
                            ) player.offhandInventory
                                .setItem(finalIndex, map)
                        } else {
                            if (checkMapItemValid(
                                    player.inventory.getUnclonedItem(finalIndex),
                                    pk
                                )
                            ) player.inventory.setItem(finalIndex, map)
                        }
                        map.sendImage(player)
                    }
                })
            }
        }
    }

    override val packetId: Int
        get() = ProtocolInfo.MAP_INFO_REQUEST_PACKET

    protected fun checkMapItemValid(item: Item?, pk: MapInfoRequestPacket): Boolean {
        return item is ItemFilledMap && item.mapId == pk.mapId
    }
}
