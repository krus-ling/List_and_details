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
    val addedAt: Long = System.currentTimeMillis()
)