package com.example.EcoWise.network

import io.github.jan.supabase.postgrest.from

class ProductService {
    private val tableName = "product_table"

    suspend fun fetchProductsFromSupabase(userId: String): List<ProductRemoteDto> {
        return try {
            SupabaseClient.client
                .from(tableName)
                .select {
                    filter { eq("userId", userId) }
                }
                .decodeList<ProductRemoteDto>()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun insertProductToSupabase(product: ProductRemoteDto) {
        try {
            SupabaseClient.client
                .from(tableName)
                .upsert(product)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun deleteProductFromSupabase(id: String, userId: String) {
        try {
            SupabaseClient.client
                .from(tableName)
                .delete {
                    filter { 
                        eq("id", id) 
                        eq("userId", userId)
                    }
                }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}