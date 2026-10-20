package com.example.listanddetails.domain.repository

import com.example.listanddetails.domain.model.LaunchDetail
import com.example.listanddetails.domain.model.LaunchItem
import kotlinx.coroutines.flow.Flow

interface FavoriteLaunchRepository {
    fun observeFavorites(): Flow<List<LaunchItem>>
    fun observeIsFavorite(id: String): Flow<Boolean>
    suspend fun isFavorite(id: String): Boolean
    suspend fun toggleFavorite(launch: LaunchItem)
    suspend fun toggleFavorite(detail: LaunchDetail)
    suspend fun removeFavorite(id: String)
}