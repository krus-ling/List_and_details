package com.example.listanddetails.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class LaunchStatusDto(
    val id: Int,
    val name: String,
    val abbrev: String?,
)