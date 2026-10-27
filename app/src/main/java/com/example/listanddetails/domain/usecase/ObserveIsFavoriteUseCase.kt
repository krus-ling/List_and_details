package com.example.listanddetails.domain.usecase

import com.example.listanddetails.domain.repository.FavoriteLaunchRepository
import kotlinx.coroutines.flow.Flow

class ObserveIsFavoriteUseCase(
    private val repository: FavoriteLaunchRepository
) {
    operator fun invoke(id: String): Flow<Boolean> = repository.observeIsFavorite(id)
}