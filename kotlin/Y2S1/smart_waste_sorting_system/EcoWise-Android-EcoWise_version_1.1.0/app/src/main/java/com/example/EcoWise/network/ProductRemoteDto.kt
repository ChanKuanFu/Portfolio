package com.example.EcoWise.network

import kotlinx.serialization.Serializable

@Serializable
data class ProductRemoteDto(
    val id: String,
    val userId: String = "", // Added for isolation
    val name: String,
    val brand: String = "",
    val category: String = "",
    val weightSize: String = "",
    val packagingMaterial: String = "",
    val isOrganic: Boolean = false,
    val ecoScore: Int = 0,
    val carbonFootprint: String = "",
    val recyclability: String = "",
    val visualType: String = "",
    val createdAt: Long,
    val isFavorite: Boolean = false,
    
    // Fields for Recycling Activity
    val isRecyclingActivity: Boolean = false,
    val centreName: String? = null,
    val centreAddress: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null
)