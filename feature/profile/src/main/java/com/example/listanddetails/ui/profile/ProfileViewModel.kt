package com.example.listanddetails.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.listanddetails.data.util.ResumeFileDownloader
import com.example.listanddetails.domain.usecase.GetProfileUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class ProfileViewModel(
    getProfileUseCase: GetProfileUseCase,
    private val resumeFileDownloader: ResumeFileDownloader
) : ViewModel() {

    val uiState: StateFlow<ProfileUiState> = getProfileUseCase()
        .map { profile -> ProfileUiState(profile = profile) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ProfileUiState(isLoading = true)
        )

    fun openResume(url: String) {
        resumeFileDownloader.downloadAndOpenResume(url)
    }
}
