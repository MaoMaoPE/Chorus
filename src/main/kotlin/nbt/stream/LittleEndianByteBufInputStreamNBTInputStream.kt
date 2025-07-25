package org.chorus_oss.chorus.nbt.stream

import org.chorus_oss.chorus.nbt.tag.*
import org.chorus_oss.chorus.utils.LittleEndianByteBufInputStream
import java.io.DataInput
import java.io.IOException

class LittleEndianByteBufInputStreamNBTInputStream(private val stream: LittleEndianByteBufInputStream) : DataInput,
    AutoCloseable {
    @Throws(IOException::class)
    override fun readUTF(): String {
        return stream.readUTF()
    }

    @Throws(IOException::class)
    override fun close() {
        stream.close()
    }

    @Throws(IOException::class)
    override fun readFully(b: ByteArray) {
        stream.readFully(b)
    }

    @Throws(IOException::class)
    override fun readFully(b: ByteArray, off: Int, len: Int) {
        stream.readFully(b, off, len)
    }

    @Throws(IOException::class)
    override fun skipBytes(n: Int): Int {
        return stream.skipBytes(n)
    }

    @Throws(IOException::class)
    override fun readBoolean(): Boolean {
        return stream.readBoolean()
    }

    @Throws(IOException::class)
    override fun readByte(): Byte {
        return stream.readByte()
    }

    @Throws(IOException::class)
    override fun readUnsignedByte(): Int {
        return stream.readUnsignedByte()
    }

    @Throws(IOException::class)
    override fun readShort(): Short {
        return stream.readShort()
    }

    @Throws(IOException::class)
    override fun readUnsignedShort(): Int {
        return stream.readUnsignedShort()
    }

    @Throws(IOException::class)
    override fun readChar(): Char {
        return stream.readChar()
    }

    @Throws(IOException::class)
    override fun readInt(): Int {
        return stream.readInt()
    }

    @Throws(IOException::class)
    override fun readLong(): Long {
        return stream.readLong()
    }

    @Throws(IOException::class)
    override fun readFloat(): Float {
        return stream.readFloat()
    }

    @Throws(IOException::class)
    override fun readDouble(): Double {
        return stream.readDouble()
    }

    @Throws(IOException::class)
    override fun readLine(): String {
        return stream.readLine()
    }

    @Throws(IOException::class)
    fun readTag(): Any? {
        return this.readTag(16)
    }

    @Throws(IOException::class)
    fun readTag(maxDepth: Int): Any {
        check(stream.available() > 0) { "Trying to read from a closed reader!" }
        val typeId = this.readUnsignedByte()
        this.readUTF()
        return this.deserialize(typeId, maxDepth)
    }

    @Throws(IOException::class)
    private fun deserialize(type: Int, maxDepth: Int): Tag<*> {
        require(maxDepth >= 0) { "NBT compound is too deeply nested" }
        val arraySize: Int
        when (type.toByte()) {
            Tag.Companion.TAG_END -> return EndTag()
            Tag.Companion.TAG_BYTE -> return ByteTag(readByte().toInt())
            Tag.Companion.TAG_SHORT -> return ShortTag(readShort().toInt())
            Tag.Companion.TAG_INT -> return IntTag(readInt())
            Tag.Companion.TAG_LONG -> return LongTag(readLong())
            Tag.Companion.TAG_FLOAT -> return FloatTag(readFloat())
            Tag.Companion.TAG_DOUBLE -> return DoubleTag(readDouble())
            Tag.Companion.TAG_BYTE_ARRAY -> {
                arraySize = this.readInt()
                val bytes = ByteArray(arraySize)
                this.readFully(bytes)
                return ByteArrayTag(bytes)
            }

            Tag.Companion.TAG_STRING -> return StringTag(this.readUTF())
            Tag.Companion.TAG_COMPOUND -> {
                val map = LinkedHashMap<String, Tag<*>>()
                var nbtType: Int
                while ((readUnsignedByte().also { nbtType = it }) != Tag.Companion.TAG_END.toInt()) {
                    val name = this.readUTF()
                    map[name] = deserialize(nbtType, maxDepth - 1)
                }
                return CompoundTag(map)
            }

            Tag.Companion.TAG_LIST -> {
                val typeId = this.readUnsignedByte()
                val listLength = this.readInt()
                val list: MutableList<Tag<*>> = ArrayList(listLength)

                var i = 0
                while (i < listLength) {
                    list.add(this.deserialize(typeId, maxDepth - 1))
                    ++i
                }
                return ListTag(typeId, list)
            }

            Tag.Companion.TAG_INT_ARRAY -> {
                arraySize = this.readInt()
                val ints = IntArray(arraySize)

                var i = 0
                while (i < arraySize) {
                    ints[i] = this.readInt()
                    ++i
                }
                return IntArrayTag(ints)
            }

            else -> throw IllegalArgumentException("Unknown type $type")
        }
    }
}
