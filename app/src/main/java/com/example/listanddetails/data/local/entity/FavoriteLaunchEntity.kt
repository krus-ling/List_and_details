package com.example.listanddetails.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_launches")
data class FavoriteLaunchEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val status: String,
    val date: String,
    val agency: String,
    val imageUrl: String?,
    val padName: String? = null,
    val hasVideo: Boolean = false,
    val rawDate: String? = null,
    val addedAt: Long = System.currentTimeMillis(),
    val rocketName: String? = null,
    val rocketImage: String? = null,
    val missionName: String? = null,
    val missionDescription: String? = null,
    val location: String? = null,
    val orbit: String? = null,
    val missionType: String? = null,
    val failReason: String? = null,
    val videoUrl: String? = null,
)
