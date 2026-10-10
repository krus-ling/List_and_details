package com.example.listanddetails.domain.model

data class LaunchItem(
    val id: String,
    val name: String,
    val status: String,
    val date: String,
    val agency: String,
    val imageUrl: String?,
    val padName: String? = null,
    val hasVideo: Boolean = false,
    val rawDate: String? = null
)
