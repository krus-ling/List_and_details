package com.example.listanddetails.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class LaunchListResponseDto(
    val next: String?,
    val results: List<LaunchListItemDto>
)
