package com.example.listanddetails.ui.details

import com.example.listanddetails.domain.model.LaunchDetail

data class LaunchDetailUiState(
    val isLoading: Boolean = false,
    val launch: LaunchDetail? = null,
    val error: String? = null,
    val isTranslated: Boolean = false,
    val isTranslating: Boolean = false,
    val translatedDescription: String? = null,
    val translatedFailReason: String? = null,
    val translatedMissionType: String? = null,
    val translatedOrbit: String? = null
)