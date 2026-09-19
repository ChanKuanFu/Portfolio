package com.example.EcoWise.network

import io.github.jan.supabase.postgrest.from

class ChallengeProgressService {

    private val tableName = "challenge_progress_table"

    suspend fun fetchProgressFromSupabase(userId: String): List<ChallengeProgressRemoteDto> {
        return try {
            SupabaseClient.client
                .from(tableName)
                .select {
                    filter { eq("userId", userId) }
                }
                .decodeList<ChallengeProgressRemoteDto>()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun insertProgressToSupabase(item: ChallengeProgressRemoteDto) {
        try {
            SupabaseClient.client
                .from(tableName)
                .insert(item)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun deleteProgressFromSupabase(challengeTitle: String, submissionDate: String, userId: String) {
        try {
            SupabaseClient.client
                .from(tableName)
                .delete {
                    filter {
                        eq("challengeTitle", challengeTitle)
                        eq("submissionDate", submissionDate)
                        eq("userId", userId)
                    }
                }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}