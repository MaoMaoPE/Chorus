package org.chorus_oss.chorus.utils

import io.netty.buffer.ByteBufAllocator
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class BinaryStreamTest {
    @Test
    fun testPutUnsignedVarInt() {
        val binaryStream = BinaryStream()
        binaryStream.putUnsignedVarInt(Integer.toUnsignedLong(-1848593788))

        val byteBuf = ByteBufAllocator.DEFAULT.ioBuffer()
        ByteBufVarInt.writeUnsignedInt(byteBuf, Integer.toUnsignedLong(-1848593788).toInt())
        val bytes = ByteArray(byteBuf.readableBytes())
        byteBuf.readBytes(bytes)
        val binaryStream2 = BinaryStream()
        binaryStream2.setBuffer(bytes)

        Assertions.assertArrayEquals(binaryStream.getBufferCopy(), bytes)
        Assertions.assertEquals(binaryStream.unsignedVarInt, binaryStream2.unsignedVarInt)
    }
}
