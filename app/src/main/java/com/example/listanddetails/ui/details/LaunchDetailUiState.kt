package com.example.listanddetails.ui.details

import com.example.listanddetails.domain.model.LaunchDetail

data class LaunchDetailUiState(
    val isLoading: Boolean = false,
    val launch: LaunchDetail? = null,
    val error: String? = null
)