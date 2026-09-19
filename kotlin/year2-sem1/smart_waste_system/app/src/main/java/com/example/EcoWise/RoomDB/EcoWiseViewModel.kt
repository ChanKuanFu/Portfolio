package com.example.EcoWise.RoomDB

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.EcoWise.data.EcoWiseRepository
import com.example.EcoWise.model.ProductItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

import kotlinx.coroutines.flow.flowOn

import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
class EcoWiseViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: EcoWiseRepository = EcoWiseRepository

    val currentUser: StateFlow<UserEntity?>
    val allProducts: StateFlow<List<ProductEntity>>
    val favoriteProducts: StateFlow<List<ProductEntity>>
    val redeemedRewards: StateFlow<List<RewardEntity>>

    init {
        val database = EcoWiseDatabase.getDatabase(application)
        repository.initialize(database.productDao(), database.userDao(), database.challengeProgressDao(), database.rewardDao())

        currentUser = repository.currentUserIdFlow.flatMapLatest { userId ->
            if (userId != null) {
                database.userDao().getUser(userId)
            } else {
                flowOf(null)
            }
        }.flowOn(Dispatchers.IO)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = null
            )

        // Dynamically bind user profile details and stats based on the active user record
        viewModelScope.launch {
            currentUser.collect { user ->
                if (user != null) {
                    repository.updateUserInfo(user.fullName, user.email)
                    // Sync the internal repository user stats with the current active user record
                    val currentStats = repository.userStats.value
                    repository.addPoints(user.ecoPoints - currentStats.ecoPoints)
                }
            }
        }

        allProducts = currentUser.flatMapLatest { user ->
            if (user != null) {
                database.productDao().getAllProducts(user.id)
            } else {
                flowOf(emptyList())
            }
        }.flowOn(Dispatchers.IO)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

        favoriteProducts = currentUser.flatMapLatest { user ->
            if (user != null) {
                database.productDao().getFavoriteProducts(user.id)
            } else {
                flowOf(emptyList())
            }
        }.flowOn(Dispatchers.IO)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

        redeemedRewards = currentUser.flatMapLatest { user ->
            if (user != null) {
                database.rewardDao().getRedeemedRewards(user.id)
            } else {
                flowOf(emptyList())
            }
        }.flowOn(Dispatchers.IO)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

        // Sync when user becomes available
        viewModelScope.launch {
            currentUser.collect { user ->
                if (user != null) {
                    syncWithCloud()
                }
            }
        }
    }

    fun logout() {
        repository.clearSession()
    }

    fun redeemShopReward(reward: com.example.EcoWise.model.ShopReward, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val success = repository.redeemReward(reward)
            onResult(success)
        }
    }

    fun updateUserProfile(fullName: String, email: String, isGoogle: Boolean) {
        viewModelScope.launch {
            try {
                val user = UserEntity(
                    id = email,
                    fullName = fullName,
                    email = email,
                    profilePictureUrl = null,
                    isGoogleAccount = isGoogle
                )
                repository.saveUserToDb(user)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    suspend fun signUp(id: String, fullName: String, email: String, password: String, isGoogle: Boolean): Boolean = withContext(Dispatchers.IO) {
        return@withContext try {
            repository.signUpUser(id, fullName, email, password, isGoogle)
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun signIn(email: String, password: String): UserEntity? = withContext(Dispatchers.IO) {
        return@withContext try {
            repository.signInUser(email, password)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun getProductItem(id: String): ProductItem? = withContext(Dispatchers.IO) {
        return@withContext try {
            repository.getProductItemById(id)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun analyzeAndSaveProduct(
        name: String,
        brand: String,
        category: String,
        weightSize: String,
        packagingMaterial: String,
        isOrganic: Boolean,
        ecoLabels: List<String>,
        onResult: (ProductItem) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val result = repository.analyzeAndSaveProduct(
                    name = name,
                    category = category,
                    brand = brand,
                    weightSize = weightSize,
                    packagingMaterial = packagingMaterial,
                    isOrganic = isOrganic,
                    ecoLabels = ecoLabels
                )
                onResult(result)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun toggleFavorite(product: ProductEntity) {
        viewModelScope.launch {
            try {
                repository.updateFavoriteStatusInDb(product.id, !product.isFavorite)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun deleteProduct(product: ProductEntity) {
        viewModelScope.launch {
            try {
                repository.deleteProductFromDb(product.id)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun syncWithCloud() {
        viewModelScope.launch {
            try {
                repository.syncFromCloud()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}