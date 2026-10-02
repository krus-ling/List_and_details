package com.example.listanddetails.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LaunchDetailDto(
    val id: String,
    val name: String,
    val status: LaunchStatusDto?,
    val net: String?,
    val image: String?,
    val mission: MissionDto?,
    val pad: PadDto?,
    val rocket: RocketDto?,
    @SerialName("failreason") val failReason: String?,
    @SerialName("launch_service_provider") val provider: LaunchProviderDto?,
    @SerialName("vidURLs") val vidUrls: List<VidUrlDto>?
)

@Serializable
data class MissionDto(
    val name: String?,
    val description: String?,
    val type: String?,
    val orbit: OrbitDto?
)

@Serializable
data class OrbitDto(
    val name: String?
)

@Serializable
data class PadDto(
    val name: String?,
    val location: LocationDto?
)

@Serializable
data class LocationDto(
    val name: String?
)

@Serializable
data class RocketDto(
    val configuration: RocketConfigDto?
)

@Serializable
data class RocketConfigDto(
    val name: String?,
    val description: String?,
    @SerialName("image_url") val imageUrl: String?
)

@Serializable
data class LaunchProviderDto(
    val name: String?
)

@Serializable
data class VidUrlDto(
    val url: String?
)