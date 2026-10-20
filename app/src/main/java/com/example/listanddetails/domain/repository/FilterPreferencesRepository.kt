package com.example.listanddetails.domain.repository

import com.example.listanddetails.domain.model.LaunchFilter
import kotlinx.coroutines.flow.Flow

interface FilterPreferencesRepository {
    fun getFilter(): Flow<LaunchFilter>
    suspend fun saveFilter(filter: LaunchFilter)
    suspend fun clearFilter()
}