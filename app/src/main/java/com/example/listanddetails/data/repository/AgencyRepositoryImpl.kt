package com.example.listanddetails.data.repository

import com.example.listanddetails.data.mapper.toDomain
import com.example.listanddetails.data.network.LaunchApiService
import com.example.listanddetails.domain.model.AgencyListResult
import com.example.listanddetails.domain.repository.AgencyRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AgencyRepositoryImpl(
    private val apiService: LaunchApiService
) : AgencyRepository {

    override suspend fun getAgencies(search: String?, limit: Int, offset: Int): Result<AgencyListResult> = withContext(Dispatchers.IO) {
        runCatching {
            val response = apiService.getAgencies(search = search, limit = limit, offset = offset)
            AgencyListResult(
                totalCount = response.count,
                items = response.results.map { it.toDomain() }
            )
        }
    }
}