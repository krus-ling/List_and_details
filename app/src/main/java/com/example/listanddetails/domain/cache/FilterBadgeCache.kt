package com.example.listanddetails.domain.cache

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class FilterBadgeCache {
    private val _hasActiveFilters = MutableStateFlow(false)
    val hasActiveFilters = _hasActiveFilters.asStateFlow()

    fun updateBadge(isActive: Boolean) {
        _hasActiveFilters.value = isActive
    }
}