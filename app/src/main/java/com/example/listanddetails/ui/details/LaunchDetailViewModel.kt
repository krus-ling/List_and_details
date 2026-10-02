package com.example.listanddetails.ui.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.listanddetails.domain.repository.LaunchRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LaunchDetailViewModel(
    private val repository: LaunchRepository,
    private val launchId: String
) : ViewModel() {

    private val _uiState = MutableStateFlow(LaunchDetailUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadLaunch()
    }

    fun loadLaunch() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            repository.getLaunchForId(launchId)
                .onSuccess { detail ->
                    _uiState.update { it.copy(isLoading = false, launch = detail) }
                }
                .onFailure { throwable ->
                    _uiState.update { it.copy(isLoading = false, error = throwable.message ?: "Неизвестная ошибка") }
                }
        }
    }
}