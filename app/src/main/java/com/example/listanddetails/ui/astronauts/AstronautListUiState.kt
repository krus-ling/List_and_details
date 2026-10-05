package com.example.listanddetails.ui.astronauts

import com.example.listanddetails.domain.model.AstronautItem

data class AstronautListUiState(
    val isLoading: Boolean = false,
    val isNextPageLoading: Boolean = false,
    val items: List<AstronautItem> = emptyList(),
    val totalCount: Int = 0,
    val searchQuery: String = "",
    val error: String? = null
)