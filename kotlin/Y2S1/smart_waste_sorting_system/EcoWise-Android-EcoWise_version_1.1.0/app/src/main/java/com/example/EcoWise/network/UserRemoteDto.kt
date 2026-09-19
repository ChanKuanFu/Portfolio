package com.example.EcoWise.network

import kotlinx.serialization.Serializable

@Serializable
data class UserRemoteDto(
    val id: String,
    val fullName: String,
    val email: String,
    val password: String = "",
    val profilePictureUrl: String? = null,
    val isGoogleAccount: Boolean = false,
    val ecoPoints: Int = 0,
    val productsAnalysed: Int = 0,
    val challengesCompleted: Int = 0
)