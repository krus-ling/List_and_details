package com.example.listanddetails.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AgencyListResponseDto(
    val count: Int = 0,
    val next: String? = null,
    val results: List<AgencyDto> = emptyList()
)

@Serializable
data class AgencyDto(
    val id: Int,
    val name: String,
    val abbrev: String? = null,
    val type: String? = null,
    @SerialName("country_code")
    val countryCode: String? = null,
    val description: String? = null,
    val administrator: String? = null,
    @SerialName("founding_year")
    val foundingYear: String? = null,
    @SerialName("logo_url")
    val logoUrl: String? = null,
    @SerialName("image_url")
    val imageUrl: String? = null
)