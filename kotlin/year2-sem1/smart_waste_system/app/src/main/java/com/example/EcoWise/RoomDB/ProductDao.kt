package com.example.EcoWise.RoomDB

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Query("SELECT * FROM product_table WHERE userId = :userId ORDER BY createdAt DESC")
    fun getAllProducts(userId: String): Flow<List<ProductEntity>>

    @Query("SELECT * FROM product_table WHERE userId = :userId AND isFavorite = 1 ORDER BY createdAt DESC")
    fun getFavoriteProducts(userId: String): Flow<List<ProductEntity>>

    @Query("SELECT * FROM product_table WHERE id = :id AND userId = :userId LIMIT 1")
    suspend fun getProductById(id: String, userId: String): ProductEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: ProductEntity)

    @Update
    suspend fun updateProduct(product: ProductEntity)

    @Delete
    suspend fun deleteProduct(product: ProductEntity)

    @Query("DELETE FROM product_table WHERE id IN (:ids) AND userId = :userId")
    suspend fun deleteProductsByIds(ids: List<String>, userId: String)

    @Query("UPDATE product_table SET isFavorite = :isFav WHERE id = :id AND userId = :userId")
    suspend fun updateFavoriteStatus(id: String, userId: String, isFav: Boolean)
}