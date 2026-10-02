package com.example.listanddetails.data.network

import com.example.listanddetails.data.dto.LaunchDetailDto
import com.example.listanddetails.data.dto.LaunchListResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface LaunchApiService {

    @GET("2.2.0/launch/")
    suspend fun getListOfLaunches(
        @Query("mode") mode: String = "list",
        @Query("search") search: String? = null,
        @Query("ordering") ordering: String = "-net"
    ) : LaunchListResponseDto

    @GET("2.2.0/launch/{id}/")
    suspend fun getDetailsLaunch(
        @Path("id") id: String
    ) : LaunchDetailDto
}