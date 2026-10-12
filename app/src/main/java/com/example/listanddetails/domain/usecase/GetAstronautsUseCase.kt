package com.example.listanddetails.domain.usecase

import com.example.listanddetails.domain.model.AstronautListResult
import com.example.listanddetails.domain.repository.AstronautRepository

class GetAstronautsUseCase(
    private val repository: AstronautRepository,
) {
    suspend operator fun invoke(
        search: String? = null,
        limit: Int = 20,
        offset: Int = 0,
    ): Result<AstronautListResult> {
        return repository.getAstronauts(search = search, limit = limit, offset = offset)
    }
}
