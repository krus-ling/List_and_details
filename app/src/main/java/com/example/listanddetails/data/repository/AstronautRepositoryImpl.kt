package com.example.listanddetails.data.repository

import com.example.listanddetails.data.cache.LaunchesMemoryCache
import com.example.listanddetails.data.mapper.toDomain
import com.example.listanddetails.data.network.LaunchApiService
import com.example.listanddetails.data.util.runCatchingCancellable
import com.example.listanddetails.domain.model.AstronautListResult
import com.example.listanddetails.domain.repository.AstronautRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AstronautRepositoryImpl(
    private val apiService: LaunchApiService,
    private val cache: LaunchesMemoryCache,
) : AstronautRepository {

    override suspend fun getAstronauts(
        search: String?,
        limit: Int,
        offset: Int
    ): Result<AstronautListResult> = withContext(Dispatchers.IO) {
        runCatchingCancellable {
            val response = apiService.getAstronauts(search = search, limit = limit, offset = offset)
            val domainItems = response.results.map { it.toDomain() }

            if (offset == 0 && search.isNullOrBlank()) {
                cache.saveAstronauts(domainItems)
            }

            AstronautListResult(
                totalCount = response.count,
                items = domainItems
            )
        }.recoverCatching { throwable ->
            val cachedAstronauts = cache.getAstronauts()
            if (cachedAstronauts.isNotEmpty() && search.isNullOrBlank()) {
                AstronautListResult(
                    totalCount = cachedAstronauts.size,
                    items = cachedAstronauts
                )
            } else {
                throw throwable
            }
        }
    }
}
