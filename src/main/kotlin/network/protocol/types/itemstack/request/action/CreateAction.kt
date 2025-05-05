package org.chorus_oss.chorus.network.protocol.types.itemstack.request.action

/**
 * CreateStackRequestActionData is sent by the client when an item is created through being used as part of a
 * recipe. For example, when milk is used to craft a cake, the buckets are leftover. The buckets are moved to
 * the slot sent by the client here.
 * Note that before this is sent, an action for consuming all items in the crafting table/grid is sent. Items
 * that are not fully consumed when used for a recipe should not be destroyed there, but instead, should be
 * turned into their respective resulting items.
 */
data class CreateAction(
    val slot: Int,
) : ItemStackRequestAction {
    override val type: ItemStackRequestActionType
        get() = ItemStackRequestActionType.CREATE
}
