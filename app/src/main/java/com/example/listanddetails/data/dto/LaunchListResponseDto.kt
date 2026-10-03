package com.example.listanddetails.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class LaunchListResponseDto(
    val count: Int = 0,
    val next: String? = null,
    val results: List<LaunchListItemDto> = emptyList()
)
