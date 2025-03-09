package org.chorus.network.protocol

import cn.nukkit.network.connection.util.HandleByteBuf
import cn.nukkit.scoreboard.data.ScorerType
import lombok.*

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
class SetScorePacket : DataPacket() {
    @JvmField
    var action: Action? = null
    @JvmField
    var infos: List<ScoreInfo> = ArrayList()

    override fun decode(byteBuf: HandleByteBuf) {
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeByte(action!!.ordinal.toByte().toInt())
        byteBuf.writeUnsignedVarInt(infos.size)

        for (info in this.infos) {
            byteBuf.writeVarLong(info.scoreboardId)
            byteBuf.writeString(info.objectiveId)
            byteBuf.writeIntLE(info.score)
            if (this.action == Action.SET) {
                byteBuf.writeByte(info.type.ordinal.toByte().toInt())
                when (info.type) {
                    ScorerType.ENTITY, ScorerType.PLAYER -> byteBuf.writeVarLong(info.entityId)
                    ScorerType.FAKE -> byteBuf.writeString(info.name!!)
                    else -> throw IllegalArgumentException("Invalid score info received")
                }
            }
        }
    }

    enum class Action {
        SET,
        REMOVE
    }

    class ScoreInfo {
        var scoreboardId: Long
        var objectiveId: String
        var score: Int
        var type: ScorerType
        var name: String?
        var entityId: Long

        constructor(scoreboardId: Long, objectiveId: String, score: Int) {
            this.scoreboardId = scoreboardId
            this.objectiveId = objectiveId
            this.score = score
            this.type = ScorerType.INVALID
            this.name = null
            this.entityId = -1
        }

        constructor(scoreboardId: Long, objectiveId: String, score: Int, name: String?) {
            this.scoreboardId = scoreboardId
            this.objectiveId = objectiveId
            this.score = score
            this.type = ScorerType.FAKE
            this.name = name
            this.entityId = -1
        }

        constructor(scoreboardId: Long, objectiveId: String, score: Int, type: ScorerType, entityId: Long) {
            this.scoreboardId = scoreboardId
            this.objectiveId = objectiveId
            this.score = score
            this.type = type
            this.entityId = entityId
            this.name = null
        }
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.SET_SCORE_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
