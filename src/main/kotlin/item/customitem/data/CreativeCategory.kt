package org.chorus_oss.chorus.item.customitem.data

/**
 * 控制自定义物品在创造栏的大分类,例如建材栏,材料栏
 * <br></br>可选值:1 CONSTRUCTOR 2 NATURE 3 EQUIPMENT 4 ITEMS 5 NONE
 *
 * @return 自定义物品的在创造栏的大分类
 * @see [bedrock wiki](https://wiki.bedrock.dev/documentation/creative-categories.html.list-of-creative-tabs)
 */
enum class CreativeCategory {
    CONSTRUCTOR,
    NATURE,
    EQUIPMENT,
    ITEMS,
    NONE;

    companion object {
        fun fromID(num: Int): CreativeCategory {
            return when (num) {
                1 -> CONSTRUCTOR
                2 -> NATURE
                3 -> EQUIPMENT
                4 -> ITEMS
                else -> NONE
            }
        }
    }
}
