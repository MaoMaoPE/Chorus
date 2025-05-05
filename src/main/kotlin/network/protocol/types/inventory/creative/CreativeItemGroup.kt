package org.chorus_oss.chorus.network.protocol.types.inventory.creative

import org.chorus_oss.chorus.item.Item

data class CreativeItemGroup(
    val category: CreativeItemCategory,
    val name: String,
    val icon: Item,
)
