package com.example.listanddetails.domain.usecase

import com.example.listanddetails.domain.model.LaunchDetail
import com.example.listanddetails.domain.repository.FavoriteLaunchRepository
import com.example.listanddetails.domain.repository.LaunchRepository

class GetLaunchDetailUseCase(
    private val launchRepository: LaunchRepository,
    private val favoriteLaunchRepository: FavoriteLaunchRepository,
) {
    suspend operator fun invoke(id: String): Result<LaunchDetail> {
        val netResult = launchRepository.getLaunchForId(id)
        if (netResult.isSuccess) {
            return netResult
        }
        val favoriteDetail = favoriteLaunchRepository.getFavoriteDetail(id)
        return if (favoriteDetail != null) {
            Result.success(favoriteDetail)
        } else {
            netResult
        }
    }
}
