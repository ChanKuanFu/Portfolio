package com.example.EcoWise.network

import kotlinx.serialization.Serializable

@Serializable
data class ChallengeProgressRemoteDto(
    val id: Int? = null,
    val userId: String = "", // Added for isolation
    val challengeId: String,
    val challengeTitle: String,
    val imagePath: String,
    val submissionDate: String,
    val timestamp: Long
)