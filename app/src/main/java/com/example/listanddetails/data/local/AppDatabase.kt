package com.example.listanddetails.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.listanddetails.data.local.dao.FavoriteLaunchDao
import com.example.listanddetails.data.local.entity.FavoriteLaunchEntity

@Database(
    entities = [FavoriteLaunchEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteLaunchDao(): FavoriteLaunchDao
}