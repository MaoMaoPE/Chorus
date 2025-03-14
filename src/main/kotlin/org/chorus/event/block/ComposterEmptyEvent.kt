package org.chorus.event.block

import org.chorus.Player
import org.chorus.block.Block
import org.chorus.event.Cancellable
import org.chorus.event.HandlerList
import org.chorus.item.Item
import org.chorus.math.Vector3
import kotlin.math.max
import kotlin.math.min

class ComposterEmptyEvent(
    block: Block,
    val player: Player,
    var itemUsed: Item,
    private var drop: Item?,
    newLevel: Int
) :
    BlockEvent(block), Cancellable {
    private var newLevel: Int

    @JvmField
    var motion: Vector3? = null


    init {
        this.newLevel = max(0.0, min(newLevel.toDouble(), 8.0)).toInt()
    }

    fun getDrop(): Item {
        return drop!!.clone()
    }

    fun setDrop(drop: Item?) {
        var drop = drop
        drop = drop?.clone() ?: Item.AIR
        this.drop = drop
    }

    fun getNewLevel(): Int {
        return newLevel
    }

    fun setNewLevel(newLevel: Int) {
        this.newLevel = max(0.0, min(newLevel.toDouble(), 8.0)).toInt()
    }

    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
