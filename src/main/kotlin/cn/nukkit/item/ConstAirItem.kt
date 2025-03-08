package cn.nukkit.item

import cn.nukkit.block.Block
import cn.nukkit.block.BlockAir
import cn.nukkit.nbt.tag.CompoundTag

class ConstAirItem : Item("minecraft:air") {
    init {
        this.meta = 0
        this.count = 0
        this.netId = 0
    }

    override var damage: Int
        get() = 0
        set(meta) {
        }

    override fun getNetId(): Int {
        return 0
    }

    override fun getCount(): Int {
        return 0
    }

    override var blockUnsafe: Block?
        get() = BlockAir()
        set(block) {
        }

    override fun getBlock(): Block {
        return BlockAir()
    }

    override fun setCompoundTag(tags: ByteArray): Item {
        return this
    }

    override fun setCompoundTag(tag: CompoundTag?): Item {
        return this
    }

    override fun setCustomBlockData(compoundTag: CompoundTag): Item {
        return this
    }

    override fun setCustomName(name: String?): Item {
        return this
    }

    override fun setLore(vararg lines: String): Item {
        return this
    }

    override fun setNamedTag(tag: CompoundTag?): Item {
        return this
    }

    override fun setRepairCost(cost: Int): Item {
        return this
    }

    override fun setCanDestroy(blocks: Array<Block>) {
    }

    override fun setCanPlaceOn(blocks: Array<Block>) {
    }

    override fun setNetId(netId: Int?) {
    }

    override fun setCount(count: Int) {
    }

    override var itemLockMode: ItemLockMode
        get() = super.itemLockMode
        set(mode) {
        }

    override fun setKeepOnDeath(keepOnDeath: Boolean) {
    }

    override val isNull: Boolean
        get() = true

    override fun clone(): Item {
        return this
    }
}
