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
package cn.nukkit.math

import kotlin.math.pow
import kotlin.math.sqrt

/**
 * @author joserobjr
 * @since 2020-09-20
 */
class ChunkVector2 @JvmOverloads constructor(var x: Int = 0, var z: Int = 0) {
    fun add(x: Int): ChunkVector2 {
        return this.add(x, 0)
    }

    fun add(x: Int, y: Int): ChunkVector2 {
        return ChunkVector2(this.x + x, this.z + y)
    }

    fun add(x: ChunkVector2): ChunkVector2 {
        return this.add(x.x, x.z)
    }

    @JvmOverloads
    fun subtract(x: Int, y: Int = 0): ChunkVector2 {
        return this.add(-x, -y)
    }

    fun subtract(x: ChunkVector2): ChunkVector2 {
        return this.add(-x.x, -x.z)
    }

    fun abs(): ChunkVector2 {
        return ChunkVector2(kotlin.math.abs(x.toDouble()).toInt(), kotlin.math.abs(z.toDouble()).toInt())
    }

    fun multiply(number: Int): ChunkVector2 {
        return ChunkVector2(this.x * number, this.z * number)
    }

    fun divide(number: Int): ChunkVector2 {
        return ChunkVector2(this.x / number, this.z / number)
    }

    @JvmOverloads
    fun distance(x: Double, y: Double = 0.0): Double {
        return sqrt(this.distanceSquared(x, y))
    }

    fun distance(vector: ChunkVector2): Double {
        return sqrt(this.distanceSquared(vector.x.toDouble(), vector.z.toDouble()))
    }

    @JvmOverloads
    fun distanceSquared(x: Double, y: Double = 0.0): Double {
        return (this.x - x).pow(2.0) + (this.z - y).pow(2.0)
    }

    fun distanceSquared(vector: ChunkVector2): Double {
        return this.distanceSquared(vector.x.toDouble(), vector.z.toDouble())
    }

    fun length(): Double {
        return sqrt(lengthSquared().toDouble())
    }

    fun lengthSquared(): Int {
        return this.x * this.x + this.z * this.z
    }

    fun dot(v: ChunkVector2): Int {
        return this.x * v.x + this.z * v.z
    }

    override fun toString(): String {
        return "MutableChunkVector(x=" + this.x + ",z=" + this.z + ")"
    }
}
