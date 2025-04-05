package org.chorus.entity.ai.memory

import org.chorus.entity.Entity
import org.chorus.entity.ai.memory.codec.IMemoryCodec
import org.chorus.utils.Identifier
import java.util.function.Supplier

class NullableMemoryType <Data : Any> (
    override val identifier: Identifier
) : IMemoryType<Data?> {
    override var codec: IMemoryCodec<Data?>? = null
        private set

    constructor(identifier: String) : this(
        Identifier(identifier)
    )

    override fun withCodec(codec: IMemoryCodec<Data?>): NullableMemoryType<Data> {
        this.codec = codec
        IMemoryType.PERSISTENT_MEMORIES.add(this)
        return this
    }

    override fun encode(entity: Entity, data: Data?) {
        codec?.encode(data, entity.namedTag!!)
    }

    override fun decode(entity: Entity): Data? {
        return codec?.decode(entity.namedTag!!)
    }

    @Suppress("UNCHECKED_CAST")
    override fun forceEncode(entity: Entity, data: Any) {
        codec?.encode(data as Data?, entity.namedTag!!)
    }

    override fun hashCode(): Int {
        return identifier.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (other === this) return true
        if (other is MemoryType<*>) {
            return identifier == other.identifier
        }
        return false
    }
}
