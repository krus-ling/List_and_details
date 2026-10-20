package com.example.listanddetails.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_launches")
data class FavoriteLaunchEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val net: String,
    val statusName: String,
    val statusDescription: String?,
    val imageUrl: String?,
    val agencyName: String?,
    val padName: String?,
    val missionDescription: String?,
    val addedAt: Long = System.currentTimeMillis()
)