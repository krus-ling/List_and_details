package com.example.listanddetails.data.repository

import com.example.listanddetails.data.mapper.toDomain
import com.example.listanddetails.data.network.LaunchApiService
import com.example.listanddetails.domain.model.LaunchDetail
import com.example.listanddetails.domain.model.LaunchListResult
import com.example.listanddetails.domain.repository.LaunchRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LaunchRepositoryImpl(
    private val apiService: LaunchApiService
) : LaunchRepository {

    override suspend fun getListOfLaunches(
        search: String?,
        ordering: String,
        netLte: String?,
        netGte: String?,
        statusId: Int?,
        limit: Int,
        offset: Int
    ): Result<LaunchListResult> = withContext(Dispatchers.IO) {
        runCatching {
            // Выполняем сетевой запрос в фоне
            val response = apiService.getListOfLaunches(
                search = search,
                ordering = ordering,
                netLte = netLte,
                netGte = netGte,
                status = statusId,
                limit = limit,
                offset = offset
            )

            // Достаем список dto и переводим в нашу модель из domain
            LaunchListResult(
                totalCount = response.count,
                items = response.results.map { it.toDomain() }
            )
        }
    }

    override suspend fun getLaunchForId(id: String): Result<LaunchDetail> = withContext(Dispatchers.IO) {
        runCatching {
            val response = apiService.getDetailsLaunch(id = id)
            response.toDomain()
        }
    }
}