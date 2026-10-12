package com.example.listanddetails.domain.model

data class LaunchDetail(
    val id: String,
    val name: String,
    val status: String,
    val date: String,
    val agency: String,
    val imageUrl: String?,
    val rocketName: String,
    val rocketImage: String?,
    val missionName: String,
    val missionDescription: String?,
    val padName: String?,
    val location: String?,
    val orbit: String?,
    val missionType: String?,
    val failReason: String?,
    val videoUrl: String?,
    val rawDate: String?
)
