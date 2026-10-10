package com.example.listanddetails.domain.repository

import com.example.listanddetails.domain.model.AgencyListResult

interface AgencyRepository {
    suspend fun getAgencies(search: String? = null, limit: Int = 20, offset: Int = 0): Result<AgencyListResult>
}