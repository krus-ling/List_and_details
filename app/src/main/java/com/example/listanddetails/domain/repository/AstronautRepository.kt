package com.example.listanddetails.domain.repository

import com.example.listanddetails.domain.model.AstronautListResult

interface AstronautRepository {
    suspend fun getAstronauts(search: String? = null, limit: Int = 20, offset: Int = 0): Result<AstronautListResult>
}