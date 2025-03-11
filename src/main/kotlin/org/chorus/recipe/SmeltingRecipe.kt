package org.chorus.recipe

import org.chorus.item.Item
import org.chorus.recipe.descriptor.ItemDescriptor

abstract class SmeltingRecipe protected constructor(id: String) : BaseRecipe(id) {
    var input: ItemDescriptor?
        get() = ingredients.first()
        set(item) {
            ingredients[0] = item
        }

    val result: Item
        get() = results.first()
}
