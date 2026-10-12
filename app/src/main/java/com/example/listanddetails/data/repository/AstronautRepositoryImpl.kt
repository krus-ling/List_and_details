package com.example.listanddetails.data.repository

import com.example.listanddetails.data.mapper.toDomain
import com.example.listanddetails.data.network.LaunchApiService
import com.example.listanddetails.domain.model.AstronautListResult
import com.example.listanddetails.domain.repository.AstronautRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AstronautRepositoryImpl(
    private val apiService: LaunchApiService
) : AstronautRepository {

    override suspend fun getAstronauts(search: String?, limit: Int, offset: Int): Result<AstronautListResult> = withContext(Dispatchers.IO) {
        runCatching {
            val response = apiService.getAstronauts(search = search, limit = limit, offset = offset)
            AstronautListResult(
                totalCount = response.count,
                items = response.results.map { it.toDomain() }
            )
        }
    }
}