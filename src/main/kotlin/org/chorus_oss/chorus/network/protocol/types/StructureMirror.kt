package org.chorus_oss.chorus.network.protocol.types

enum class StructureMirror {
    NONE,
    X,
    Z,
    XZ;

    companion object {
        private val VALUES = entries.toTypedArray()

        fun from(id: Int): StructureMirror {
            return VALUES[id]
        }
    }
}
