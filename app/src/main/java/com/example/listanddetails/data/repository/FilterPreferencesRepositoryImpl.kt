package com.example.listanddetails.data.repository

import com.example.listanddetails.data.datastore.FilterPreferencesDataSource
import com.example.listanddetails.domain.model.LaunchFilter
import com.example.listanddetails.domain.repository.FilterPreferencesRepository
import kotlinx.coroutines.flow.Flow

class FilterPreferencesRepositoryImpl(
    private val dataSource: FilterPreferencesDataSource
) : FilterPreferencesRepository {

    override fun getFilter(): Flow<LaunchFilter> = dataSource.filterFlow

    override suspend fun saveFilter(filter: LaunchFilter) {
        dataSource.saveFilter(filter)
    }

    override suspend fun clearFilter() {
        dataSource.clearFilter()
    }
}