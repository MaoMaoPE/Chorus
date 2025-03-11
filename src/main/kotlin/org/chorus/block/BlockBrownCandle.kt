package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockBrownCandle @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockCandle(blockstate) {
    override fun toCakeForm(): Block {
        return BlockBrownCandleCake()
    }

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.BROWN_CANDLE, CommonBlockProperties.CANDLES, CommonBlockProperties.LIT)
    }
}