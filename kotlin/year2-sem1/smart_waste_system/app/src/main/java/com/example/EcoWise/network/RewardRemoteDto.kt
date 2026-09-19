package com.example.EcoWise.network

import kotlinx.serialization.Serializable

@Serializable
data class RewardRemoteDto(
    val id: String,
    val userId: String,
    val title: String,
    val dateFormatted: String,
    val status: String
)