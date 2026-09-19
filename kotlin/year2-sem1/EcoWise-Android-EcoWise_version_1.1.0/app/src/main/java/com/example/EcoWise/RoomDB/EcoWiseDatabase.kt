package com.example.EcoWise.RoomDB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        ChallengeProgressEntity::class,
        UserEntity::class,
        ProductEntity::class,
        RewardEntity::class
    ],
    version = 7,
    exportSchema = false
)
abstract class EcoWiseDatabase : RoomDatabase() {

    abstract fun challengeProgressDao(): ChallengeProgressDao
    abstract fun userDao(): UserDao
    abstract fun productDao(): ProductDao
    abstract fun rewardDao(): RewardDao

    companion object {
        @Volatile
        private var INSTANCE: EcoWiseDatabase? = null

        fun getDatabase(context: Context): EcoWiseDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = try {
                    val db = buildDatabase(context)
                    // Force open to verify schema integrity immediately
                    db.openHelper.writableDatabase
                    db
                } catch (e: Exception) {
                    // If building fails (e.g. corruption, schema mismatch), delete and retry
                    context.deleteDatabase("ecowise_database")
                    buildDatabase(context)
                }
                INSTANCE = instance
                instance
            }
        }

        private fun buildDatabase(context: Context): EcoWiseDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                EcoWiseDatabase::class.java,
                "ecowise_database"
            )
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}