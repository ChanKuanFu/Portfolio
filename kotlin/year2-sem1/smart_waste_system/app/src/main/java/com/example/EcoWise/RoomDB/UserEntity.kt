package com.example.EcoWise.RoomDB

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class UserEntity(
    @PrimaryKey
    val id: String,
    val fullName: String,
    val email: String,
    val password: String = "",
    val profilePictureUrl: String?,
    val isGoogleAccount: Boolean,
    val ecoPoints: Int = 0,
    val productsAnalysed: Int = 0,
    val challengesCompleted: Int = 0
)