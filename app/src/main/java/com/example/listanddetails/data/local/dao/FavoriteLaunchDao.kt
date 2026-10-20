package com.example.listanddetails.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.listanddetails.data.local.entity.FavoriteLaunchEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface FavoriteLaunchDao {
    @Query("SELECT * FROM favorite_launches ORDER BY addedAt DESC")
    fun observeAll(): Flow<List<FavoriteLaunchEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_launches WHERE id = :id)")
    fun observeIsFavorite(id: String): Flow<Boolean>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_launches WHERE id = :id)")
    suspend fun isFavorite(id: String): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(launch: FavoriteLaunchEntity)

    @Query("DELETE FROM favorite_launches WHERE id = :id")
    suspend fun deleteById(id: String)
}