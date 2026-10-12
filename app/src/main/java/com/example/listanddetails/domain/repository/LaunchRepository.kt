package com.example.listanddetails.domain.repository

import com.example.listanddetails.domain.model.LaunchDetail
import com.example.listanddetails.domain.model.LaunchListResult

interface LaunchRepository {

    suspend fun getListOfLaunches(
        search: String? = null,
        ordering: String = "-net",
        netLte: String? = null,
        netGte: String? = null,
        statusId: Int? = null,
        limit: Int = 15,
        offset: Int = 0
    ) : Result<LaunchListResult>

    suspend fun getLaunchForId(
        id: String
    ) : Result<LaunchDetail>
}