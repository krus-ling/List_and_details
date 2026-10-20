package com.example.listanddetails.ui.favorites

import com.example.listanddetails.domain.model.LaunchItem

data class FavoritesUiState(
    val items: List<LaunchItem> = emptyList(),
    val isLoading: Boolean = false
)