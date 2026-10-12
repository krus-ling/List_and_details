package com.example.listanddetails.ui.list

import com.example.listanddetails.domain.model.LaunchFilter
import com.example.listanddetails.domain.model.LaunchItem

data class LaunchListUiState(
    val isLoading: Boolean = false,
    val isNextPageLoading: Boolean = false,
    val items: List<LaunchItem> = emptyList(),
    val totalCount: Int = 0,
    val searchQuery: String = "",
    val error: String? = null,
    val cooldownSeconds: Int = 0,
    val initialCooldownSeconds: Int = 0,
    val filter: LaunchFilter = LaunchFilter(),
    val isFilterSheetOpen: Boolean = false
)
