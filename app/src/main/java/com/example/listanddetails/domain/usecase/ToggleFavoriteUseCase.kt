package com.example.listanddetails.domain.usecase

import com.example.listanddetails.domain.model.LaunchDetail
import com.example.listanddetails.domain.model.LaunchItem
import com.example.listanddetails.domain.repository.FavoriteLaunchRepository

class ToggleFavoriteUseCase(
    private val repository: FavoriteLaunchRepository
) {
    suspend operator fun invoke(launch: LaunchItem) = repository.toggleFavorite(launch)
    suspend operator fun invoke(detail: LaunchDetail) = repository.toggleFavorite(detail)
}