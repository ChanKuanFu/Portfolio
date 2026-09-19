package com.example.EcoWise.network

import io.github.jan.supabase.postgrest.from

class UserService {
    private val tableName = "user_table"

    suspend fun fetchUserFromSupabase(email: String): UserRemoteDto? {
        val cleanEmail = email.trim().lowercase()
        return try {
            SupabaseClient.client
                .from(tableName)
                .select {
                    filter { eq("email", cleanEmail) }
                }
                .decodeList<UserRemoteDto>()
                .firstOrNull()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun upsertUserToSupabase(user: UserRemoteDto): Boolean {
        return try {
            SupabaseClient.client
                .from(tableName)
                .upsert(user)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}