package com.example.listanddetails.domain.model

data class LaunchListResult(
    val totalCount: Int,
    val items: List<LaunchItem>
)
