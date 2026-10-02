package com.example.listanddetails.ui.list

import com.example.listanddetails.domain.model.LaunchItem

data class LaunchListUiState(
    val isLoading: Boolean = false,
    val items: List<LaunchItem> = emptyList(),
    val error: String? = null,
    val searchQuery: String = "",
    val selectedFilter: String? = null
)