package com.example.EcoWise.network

import io.github.jan.supabase.postgrest.from

class RewardService {
    private val tableName = "reward_table"

    suspend fun fetchRewardsFromSupabase(userId: String): List<RewardRemoteDto> {
        return try {
            SupabaseClient.client
                .from(tableName)
                .select {
                    filter { eq("userId", userId) }
                }
                .decodeList<RewardRemoteDto>()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun insertRewardToSupabase(reward: RewardRemoteDto) {
        try {
            SupabaseClient.client
                .from(tableName)
                .insert(reward)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}