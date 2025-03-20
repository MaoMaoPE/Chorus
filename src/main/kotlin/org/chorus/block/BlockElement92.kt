package org.chorus.block

class BlockElement92 @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    Block(blockstate,) {
        
    override val properties: BlockProperties
        get() = Companion.properties
    
    companion object {
        val properties: BlockProperties = BlockProperties("minecraft:element_92")
    }
}