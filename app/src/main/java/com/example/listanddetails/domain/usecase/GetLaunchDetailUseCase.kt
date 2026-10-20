package com.example.listanddetails.domain.usecase

import com.example.listanddetails.domain.model.LaunchDetail
import com.example.listanddetails.domain.repository.LaunchRepository

class GetLaunchDetailUseCase(
    private val repository: LaunchRepository,
) {
    suspend operator fun invoke(id: String): Result<LaunchDetail> {
        return repository.getLaunchForId(id)
    }
}
