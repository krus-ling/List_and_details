package com.example.listanddetails.domain.usecase

import com.example.listanddetails.domain.model.LaunchItem
import com.example.listanddetails.domain.repository.FavoriteLaunchRepository
import kotlinx.coroutines.flow.Flow

class GetFavoritesUseCase(
    private val repository: FavoriteLaunchRepository
) {
    operator fun invoke(): Flow<List<LaunchItem>> = repository.observeFavorites()
}