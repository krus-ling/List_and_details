package com.example.listanddetails.data.repository

import com.example.listanddetails.data.cache.LaunchesMemoryCache
import com.example.listanddetails.data.mapper.toDomain
import com.example.listanddetails.data.network.LaunchApiService
import com.example.listanddetails.data.util.runCatchingCancellable
import com.example.listanddetails.domain.model.AgencyListResult
import com.example.listanddetails.domain.repository.AgencyRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AgencyRepositoryImpl(
    private val apiService: LaunchApiService,
    private val cache: LaunchesMemoryCache,
) : AgencyRepository {

    override suspend fun getAgencies(
        search: String?,
        limit: Int,
        offset: Int
    ): Result<AgencyListResult> = withContext(Dispatchers.IO) {
        runCatchingCancellable {
            val response = apiService.getAgencies(search = search, limit = limit, offset = offset)
            val domainItems = response.results.map { it.toDomain() }

            if (offset == 0 && search.isNullOrBlank()) {
                cache.saveAgencies(domainItems)
            }

            AgencyListResult(
                totalCount = response.count,
                items = domainItems
            )
        }.recoverCatching { throwable ->
            val cachedAgencies = cache.getAgencies()
            if (cachedAgencies.isNotEmpty() && search.isNullOrBlank()) {
                AgencyListResult(
                    totalCount = cachedAgencies.size,
                    items = cachedAgencies
                )
            } else {
                throw throwable
            }
        }
    }
}
