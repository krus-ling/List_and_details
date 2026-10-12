package com.example.listanddetails.domain.usecase

import com.example.listanddetails.domain.model.AgencyListResult
import com.example.listanddetails.domain.repository.AgencyRepository

class GetAgenciesUseCase(
    private val repository: AgencyRepository,
) {
    suspend operator fun invoke(
        search: String? = null,
        limit: Int = 20,
        offset: Int = 0,
    ): Result<AgencyListResult> {
        return repository.getAgencies(search = search, limit = limit, offset = offset)
    }
}
