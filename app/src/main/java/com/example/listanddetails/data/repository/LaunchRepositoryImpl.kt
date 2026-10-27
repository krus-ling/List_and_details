package com.example.listanddetails.data.repository

import com.example.listanddetails.data.cache.LaunchesMemoryCache
import com.example.listanddetails.data.local.dao.FavoriteLaunchDao
import com.example.listanddetails.data.mapper.toDetail
import com.example.listanddetails.data.mapper.toDomain
import com.example.listanddetails.data.network.LaunchApiService
import com.example.listanddetails.data.util.runCatchingCancellable
import com.example.listanddetails.domain.model.LaunchDetail
import com.example.listanddetails.domain.model.LaunchListResult
import com.example.listanddetails.domain.repository.LaunchRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LaunchRepositoryImpl(
    private val apiService: LaunchApiService,
    private val cache: LaunchesMemoryCache,
    private val favoriteLaunchDao: FavoriteLaunchDao,
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
        runCatchingCancellable {
            val response = apiService.getListOfLaunches(
                search = search,
                ordering = ordering,
                netLte = netLte,
                netGte = netGte,
                status = statusId,
                limit = limit,
                offset = offset
            )

            val domainItems = response.results.map { it.toDomain() }
            cache.saveLaunches(domainItems)

            LaunchListResult(
                totalCount = response.count,
                items = domainItems
            )
        }
    }

    override suspend fun getLaunchForId(id: String): Result<LaunchDetail> = withContext(Dispatchers.IO) {
        // 1) Проверяем memory cache для деталей
        cache.getLaunchDetail(id)?.let { cachedDetail ->
            return@withContext Result.success(cachedDetail)
        }

        // 2) Запрашиваем из API
        runCatchingCancellable {
            val response = apiService.getDetailsLaunch(id = id)
            val detail = response.toDomain()
            cache.saveLaunchDetail(detail)
            detail
        }.recoverCatching { throwable ->
            // 3) Офлайн-фоллбэк: проверяем кэш списка или Room БД
            val cachedItem = cache.getLaunchItem(id)
            if (cachedItem != null) {
                return@recoverCatching cachedItem.toDetail()
            }

            val favoriteEntity = favoriteLaunchDao.getFavoriteById(id)
            if (favoriteEntity != null) {
                return@recoverCatching favoriteEntity.toDetail()
            }

            throw throwable
        }
    }
}
