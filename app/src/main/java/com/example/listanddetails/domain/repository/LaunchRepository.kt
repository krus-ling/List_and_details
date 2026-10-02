package com.example.listanddetails.domain.repository

import com.example.listanddetails.domain.model.LaunchDetail
import com.example.listanddetails.domain.model.LaunchItem

interface LaunchRepository {

    suspend fun getListOfLaunches(
        query: String? = null
    ) : Result<List<LaunchItem>>

    suspend fun getLaunchForId(
        id: String
    ) : Result<LaunchDetail>
}