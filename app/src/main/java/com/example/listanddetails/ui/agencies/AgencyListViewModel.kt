package com.example.listanddetails.ui.agencies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.listanddetails.domain.repository.AgencyRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

private const val PAGE_LIMIT = 20
private const val SEARCH_DEBOUNCE_MS = 500L

class AgencyListViewModel(
    private val repository: AgencyRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AgencyListUiState())
    val uiState = _uiState.asStateFlow()

    private var searchJob: Job? = null
    private var serverOffset = 0

    init {
        loadAgencies()
    }

    fun loadAgencies() {
        serverOffset = 0
        val query = _uiState.value.searchQuery.takeIf { it.isNotBlank() }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            repository.getAgencies(search = query, limit = PAGE_LIMIT, offset = 0)
                .onSuccess { result ->
                    serverOffset = result.items.size
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            items = result.items,
                            totalCount = result.totalCount
                        )
                    }
                }
                .onFailure { throwable ->
                    _uiState.update {
                        it.copy(isLoading = false, error = throwable.message)
                    }
                }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.update { it.copy(searchQuery = query, items = emptyList()) }
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            if (query.isNotBlank()) {
                delay(SEARCH_DEBOUNCE_MS.milliseconds)
            }
            loadAgencies()
        }
    }

    fun loadNextPage() {
        val state = _uiState.value
        if (state.isLoading || state.isNextPageLoading) return
        if (state.totalCount in 1..serverOffset) return

        val query = state.searchQuery.takeIf { it.isNotBlank() }

        viewModelScope.launch {
            _uiState.update { it.copy(isNextPageLoading = true) }
            repository.getAgencies(search = query, limit = PAGE_LIMIT, offset = serverOffset)
                .onSuccess { result ->
                    serverOffset += result.items.size
                    _uiState.update {
                        it.copy(
                            isNextPageLoading = false,
                            items = it.items + result.items,
                            totalCount = result.totalCount
                        )
                    }
                }
                .onFailure {
                    _uiState.update { it.copy(isNextPageLoading = false) }
                }
        }
    }
}