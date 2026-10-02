package com.example.listanddetails.data.repository

import com.example.listanddetails.data.mapper.toDomain
import com.example.listanddetails.data.network.LaunchApiService
import com.example.listanddetails.domain.model.LaunchDetail
import com.example.listanddetails.domain.model.LaunchItem
import com.example.listanddetails.domain.repository.LaunchRepository

class LaunchRepositoryImpl(
    private val apiService: LaunchApiService
) : LaunchRepository {
    override suspend fun getListOfLaunches(query: String?): Result<List<LaunchItem>> = runCatching {
        // Делаем сетевой запрос
        val response = apiService.getListOfLaunches(search = query)

        // Достаем список dto и переводим в нашу модель из domain
        response.results.map { it.toDomain() }
    }

    override suspend fun getLaunchForId(id: String): Result<LaunchDetail> = runCatching {
        val response = apiService.getDetailsLaunch(id = id)
        response.toDomain()
    }
}