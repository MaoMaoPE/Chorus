package org.chorus_oss.chorus.registry

import org.chorus_oss.chorus.network.DataPacket
import org.chorus_oss.chorus.network.PacketDecoder
import org.chorus_oss.chorus.network.ProtocolInfo
import org.chorus_oss.chorus.network.protocol.*
import java.util.concurrent.atomic.AtomicBoolean

class PacketDecoderRegistry : IRegistry<Int, PacketDecoder<out DataPacket>?, PacketDecoder<out DataPacket>> {
    private val packets = mutableMapOf<Int, PacketDecoder<out DataPacket>>()
    private val initialized = AtomicBoolean(false)

    override fun init() {
        if (initialized.getAndSet(true)) return

        // Register all packets that are Client -> Server
        register(ProtocolInfo.INVENTORY_TRANSACTION_PACKET, InventoryTransactionPacket)
        register(ProtocolInfo.ANIMATE_PACKET, AnimatePacket)
        register(ProtocolInfo.ENTITY_EVENT_PACKET, EntityEventPacket)
        register(ProtocolInfo.LOGIN_PACKET, LoginPacket)
        register(ProtocolInfo.MODAL_FORM_RESPONSE_PACKET, ModalFormResponsePacket)
        register(ProtocolInfo.MOVE_ENTITY_ABSOLUTE_PACKET, MoveEntityAbsolutePacket)
        register(ProtocolInfo.MOVE_PLAYER_PACKET, MovePlayerPacket)
        register(ProtocolInfo.PLAYER_ACTION_PACKET, PlayerActionPacket)
        register(ProtocolInfo.PLAYER_SKIN_PACKET, PlayerSkinPacket)
        register(ProtocolInfo.SET_TITLE_PACKET, SetTitlePacket)
        register(ProtocolInfo.MOVE_ENTITY_DELTA_PACKET, MoveEntityDeltaPacket)
        register(ProtocolInfo.LEVEL_SOUND_EVENT_PACKET, LevelSoundEventPacket)
        register(ProtocolInfo.NPC_REQUEST_PACKET, NPCRequestPacket)
        register(ProtocolInfo.STRUCTURE_BLOCK_UPDATE_PACKET, StructureBlockUpdatePacket)
    }

    override operator fun get(key: Int): PacketDecoder<out DataPacket>? {
        return packets[key]
    }

    override fun reload() {
        initialized.set(false)
        packets.clear()
        init()
    }

    @Throws(RegisterException::class)
    override fun register(key: Int, value: PacketDecoder<*>) {
        packets[key] = value
    }
}