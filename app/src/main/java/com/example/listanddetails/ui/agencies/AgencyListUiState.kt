package com.example.listanddetails.ui.agencies

import com.example.listanddetails.domain.model.AgencyItem

data class AgencyListUiState(
    val isLoading: Boolean = false,
    val isNextPageLoading: Boolean = false,
    val items: List<AgencyItem> = emptyList(),
    val totalCount: Int = 0,
    val searchQuery: String = "",
    val error: String? = null
)