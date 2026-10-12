package com.example.listanddetails.data.network

import com.example.listanddetails.data.dto.AgencyListResponseDto
import com.example.listanddetails.data.dto.AstronautListResponseDto
import com.example.listanddetails.data.dto.LaunchDetailDto
import com.example.listanddetails.data.dto.LaunchListResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface LaunchApiService {

    @GET("2.2.0/launch/")
    suspend fun getListOfLaunches(
        @Query("mode") mode: String = "detailed",
        @Query("search") search: String? = null,
        @Query("ordering") ordering: String = "-net",
        @Query("net__lte") netLte: String? = null,
        @Query("net__gte") netGte: String? = null,
        @Query("status") status: Int? = null,
        @Query("limit") limit: Int = 15,
        @Query("offset") offset: Int = 0
    ) : LaunchListResponseDto

    @GET("2.2.0/launch/{id}/")
    suspend fun getDetailsLaunch(
        @Path("id") id: String
    ) : LaunchDetailDto

    @GET("2.2.0/agencies/")
    suspend fun getAgencies(
        @Query("search") search: String? = null,
        @Query("limit") limit: Int = 20,
        @Query("offset") offset: Int = 0
    ): AgencyListResponseDto

    @GET("2.2.0/astronaut/")
    suspend fun getAstronauts(
        @Query("search") search: String? = null,
        @Query("limit") limit: Int = 20,
        @Query("offset") offset: Int = 0
    ): AstronautListResponseDto
}