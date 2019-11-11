package com.marklynch.compose.data

data class ManualLocation(
    val id: Long,
    val displayName: String,
    val latitude: Double,
    val longitude: Double
) {
    override fun toString(): String {
        return displayName
    }
}