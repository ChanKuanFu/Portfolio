package com.example.EcoWise.RoomDB

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reward_table")
data class RewardEntity(
    @PrimaryKey
    val id: String,
    val userId: String,
    val title: String,
    val dateFormatted: String,
    val status: String
)