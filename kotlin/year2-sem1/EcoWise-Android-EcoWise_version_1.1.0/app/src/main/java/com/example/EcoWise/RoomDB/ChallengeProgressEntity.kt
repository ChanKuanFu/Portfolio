package com.example.EcoWise.RoomDB

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "challenge_progress_table")
data class ChallengeProgressEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: String = "", // Added for multi-user isolation
    val challengeId: String,
    val challengeTitle: String,
    val imagePath: String,
    val submissionDate: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isSubmitted: Boolean = true,
    val isSynced: Boolean = false
)