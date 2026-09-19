package com.example.EcoWise.RoomDB

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "product_table")
data class ProductEntity(
    @PrimaryKey
    val id: String,
    val userId: String = "", // Added for multi-user isolation
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
    val createdAt: Long = System.currentTimeMillis(),
    val isFavorite: Boolean = false,
    
    // Fields for Recycling Activity
    val isRecyclingActivity: Boolean = false,
    val centreName: String? = null,
    val centreAddress: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null
)