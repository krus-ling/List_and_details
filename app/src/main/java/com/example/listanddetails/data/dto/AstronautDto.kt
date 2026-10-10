package com.example.listanddetails.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AstronautListResponseDto(
    val count: Int = 0,
    val next: String? = null,
    val results: List<AstronautDto> = emptyList()
)

@Serializable
data class AstronautDto(
    val id: Int,
    val name: String,
    val status: AstronautStatusDto? = null,
    val agency: AstronautAgencyDto? = null,
    val nationality: String? = null,
    @SerialName("profile_image")
    val profileImage: String? = null,
    val bio: String? = null,
    @SerialName("date_of_birth")
    val dateOfBirth: String? = null
)

@Serializable
data class AstronautStatusDto(
    val id: Int? = null,
    val name: String? = null
)

@Serializable
data class AstronautAgencyDto(
    val id: Int? = null,
    val name: String? = null,
    val abbrev: String? = null
)