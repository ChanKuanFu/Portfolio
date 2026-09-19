package com.example.EcoWise.RoomDB

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ChallengeProgressDao {

    @Query("SELECT * FROM challenge_progress_table WHERE userId = :userId ORDER BY id DESC")
    fun getAllChallengeProgress(userId: String): Flow<List<ChallengeProgressEntity>>

    @Query("SELECT * FROM challenge_progress_table WHERE challengeId = :challengeId AND userId = :userId ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLastProgressForChallenge(challengeId: String, userId: String): ChallengeProgressEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProgress(item: ChallengeProgressEntity)

    @Delete
    suspend fun deleteProgress(item: ChallengeProgressEntity)

    @Query("DELETE FROM challenge_progress_table WHERE id = :id AND userId = :userId")
    suspend fun deleteProgressById(id: Int, userId: String)
}