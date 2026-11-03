package com.example.listanddetails.ui.profile

import com.example.listanddetails.domain.model.Profile

data class ProfileUiState(
    val profile: Profile = Profile(),
    val isLoading: Boolean = false
)
