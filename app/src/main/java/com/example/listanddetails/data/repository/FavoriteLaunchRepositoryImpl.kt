package com.example.listanddetails.data.repository

import com.example.listanddetails.data.local.dao.FavoriteLaunchDao
import com.example.listanddetails.data.mapper.toDomain
import com.example.listanddetails.data.mapper.toFavoriteEntity
import com.example.listanddetails.domain.model.LaunchDetail
import com.example.listanddetails.domain.model.LaunchItem
import com.example.listanddetails.domain.repository.FavoriteLaunchRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class FavoriteLaunchRepositoryImpl(
    private val favoriteDao: FavoriteLaunchDao
) : FavoriteLaunchRepository {

    override fun observeFavorites(): Flow<List<LaunchItem>> {
        return favoriteDao.observeAll().map { list ->
            list.map { it.toDomain() }
        }
    }

    override fun observeIsFavorite(id: String): Flow<Boolean> {
        return favoriteDao.observeIsFavorite(id)
    }

    override suspend fun isFavorite(id: String): Boolean = withContext(Dispatchers.IO) {
        favoriteDao.isFavorite(id)
    }

    override suspend fun toggleFavorite(launch: LaunchItem) = withContext(Dispatchers.IO) {
        if (favoriteDao.isFavorite(launch.id)) {
            favoriteDao.deleteById(launch.id)
        } else {
            favoriteDao.insert(launch.toFavoriteEntity())
        }
    }

    override suspend fun toggleFavorite(detail: LaunchDetail) = withContext(Dispatchers.IO) {
        if (favoriteDao.isFavorite(detail.id)) {
            favoriteDao.deleteById(detail.id)
        } else {
            favoriteDao.insert(detail.toFavoriteEntity())
        }
    }

    override suspend fun removeFavorite(id: String) = withContext(Dispatchers.IO) {
        favoriteDao.deleteById(id)
    }
}