package com.example.listanddetails.domain.model

data class AstronautItem(
    val id: Int,
    val name: String,
    val status: String?,
    val agencyName: String?,
    val nationality: String?,
    val profileImage: String?,
    val bio: String?,
    val dateOfBirth: String?
)

data class AstronautListResult(
    val totalCount: Int,
    val items: List<AstronautItem>
)