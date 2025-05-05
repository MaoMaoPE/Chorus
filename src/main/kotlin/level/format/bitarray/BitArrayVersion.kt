package org.chorus_oss.chorus.level.format.bitarray

import kotlin.math.ceil

enum class BitArrayVersion(bits: Int, entriesPerWord: Int, val next: BitArrayVersion?) {
    V16(16, 2, null),
    V8(8, 4, V16),
    V6(6, 5, V8),  // 2 bit padding
    V5(5, 6, V6),  // 2 bit padding
    V4(4, 8, V5),
    V3(3, 10, V4),  // 2 bit padding
    V2(2, 16, V3),
    V1(1, 32, V2),
    V0(0, 0, V1);

    val bits: Byte = bits.toByte()
    val entriesPerWord: Byte = entriesPerWord.toByte()
    val maxEntryValue: Int = (1 shl this.bits.toInt()) - 1

    fun getWordsForSize(size: Int): Int {
        return ceil(size.toFloat() / this.entriesPerWord).toInt()
    }

    @JvmOverloads
    fun createArray(
        size: Int,
        words: IntArray = IntArray(ceil(size.toFloat() / this.entriesPerWord).toInt())
    ): BitArray {
        return if (this == V3 || this == V5 || this == V6) PaddedBitArray(
            this, size, words
        )
        else if (this == V0) SingletonBitArray()
        else Pow2BitArray(this, size, words)
    }

    companion object {
        private val VALUES = entries.toTypedArray()

        fun get(version: Int, read: Boolean): BitArrayVersion? {
            for (ver in entries) if ((!read && ver.entriesPerWord <= version) || (read && ver.bits.toInt() == version)) return ver

            if (version == 0x7F && read) return null
            throw IllegalArgumentException("Invalid palette version: $version")
        }

        fun forBitsCeil(bits: Int): BitArrayVersion? {
            for (i in VALUES.indices.reversed()) {
                val version = VALUES[i]
                if (version.bits >= bits) return version
            }

            return null
        }
    }
}
