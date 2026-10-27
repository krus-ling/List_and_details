package com.example.listanddetails.ui.astronauts

import com.example.listanddetails.domain.model.AstronautItem

data class AstronautListUiState(
    val isLoading: Boolean = true,
    val isNextPageLoading: Boolean = false,
    val items: List<AstronautItem> = emptyList(),
    val totalCount: Int = 0,
    val searchQuery: String = "",
    val error: String? = null,
    val cooldownSeconds: Int = 0,
    val initialCooldownSeconds: Int = 0,
)
