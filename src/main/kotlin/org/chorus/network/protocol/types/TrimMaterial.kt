package org.chorus.network.protocol.types

@JvmRecord
data class TrimMaterial(
    val materialId: String,
    val color: String,
    val itemName: String
)
