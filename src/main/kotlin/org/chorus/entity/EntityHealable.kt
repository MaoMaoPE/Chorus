package org.chorus.entity

import org.chorus.item.Item

/**
 * 实体可通过喂食食物被治疗
 */
interface EntityHealable {
    /**
     * 获得可以治疗食物的治疗量
     */
    fun getHealingAmount(item: Item): Int
}
