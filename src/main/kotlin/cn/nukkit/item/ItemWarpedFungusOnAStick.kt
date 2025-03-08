/*
 * https://PowerNukkit.org - The Nukkit you know but Powerful!
 * Copyright (C) 2020  José Roberto de Araújo Júnior
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package cn.nukkit.item

/**
 * @author joserobjr
 * @since 2021-02-16
 */
class ItemWarpedFungusOnAStick @JvmOverloads constructor(meta: Int = 0, count: Int = 1) :
    ItemTool(ItemID.Companion.WARPED_FUNGUS_ON_A_STICK, meta, count, "Warped Fungus on a Stick") {
    override val maxStackSize: Int
        get() = 1

    override val maxDurability: Int
        get() = ItemTool.Companion.DURABILITY_WARPED_FUNGUS_ON_A_STICK

    override fun noDamageOnBreak(): Boolean {
        return true
    }
}
