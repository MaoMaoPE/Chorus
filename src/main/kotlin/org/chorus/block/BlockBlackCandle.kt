package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockBlackCandle @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockCandle(blockstate) {
    override fun toCakeForm(): Block {
        return BlockBlackCandleCake()
    }

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.BLACK_CANDLE, CommonBlockProperties.CANDLES, CommonBlockProperties.LIT)
    }
}