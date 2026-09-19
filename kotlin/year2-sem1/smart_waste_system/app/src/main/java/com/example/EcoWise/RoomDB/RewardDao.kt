package com.example.EcoWise.RoomDB

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RewardDao {
    @Query("SELECT * FROM reward_table WHERE userId = :userId ORDER BY dateFormatted DESC")
    fun getRedeemedRewards(userId: String): Flow<List<RewardEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReward(reward: RewardEntity)

    @Query("DELETE FROM reward_table WHERE userId = :userId")
    suspend fun clearRewardsForUser(userId: String)
}