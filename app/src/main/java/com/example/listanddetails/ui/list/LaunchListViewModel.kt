package com.example.listanddetails.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.listanddetails.domain.repository.LaunchRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LaunchListViewModel(
    private val repository: LaunchRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LaunchListUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadLaunches()
    }

    fun loadLaunches(search: String? = _uiState.value.selectedFilter) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null, selectedFilter = search) }
            repository.getListOfLaunches(query = search)
                .onSuccess { launches ->
                    _uiState.update { it.copy(isLoading = false, items = launches) }
                }
                .onFailure { throwable ->
                    _uiState.update { it.copy(
                        isLoading = false,
                        error = throwable.message ?: "Неизвестная ошибка"
                    ) }
                }
        }
    }
}