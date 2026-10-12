package com.example.listanddetails.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.listanddetails.data.mapper.toAppError
import com.example.listanddetails.domain.model.AppError
import com.example.listanddetails.domain.model.LaunchFilter
import com.example.listanddetails.domain.usecase.GetLaunchesUseCase
import com.example.listanddetails.ui.components.handleAppError
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

private const val SEARCH_DEBOUNCE_MS = 500L

class LaunchListViewModel(
    private val getLaunchesUseCase: GetLaunchesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LaunchListUiState())
    val uiState = _uiState.asStateFlow()

    private var cooldownJob: Job? = null
    private var searchJob: Job? = null
    private var serverOffset = 0

    init {
        loadLaunches()
    }

    fun loadLaunches() {
        cooldownJob?.cancel()
        serverOffset = 0

        val currentFilter = _uiState.value.filter

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null, cooldownSeconds = 0) }

            getLaunchesUseCase(
                filter = currentFilter,
                searchQuery = _uiState.value.searchQuery,
                offset = 0,
                isInitialPage = true
            ).onSuccess { result ->
                serverOffset = result.fetchedCount
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        items = result.items,
                        totalCount = result.totalCount
                    )
                }
            }.onFailure { throwable ->
                handleError(throwable)
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            if (query.isNotBlank()) {
                delay(SEARCH_DEBOUNCE_MS.milliseconds)
            }
            loadLaunches()
        }
    }

    fun loadNextPage() {
        var shouldFetch = false
        var currentFilter = LaunchFilter()
        var currentSearchQuery = ""

        _uiState.update { state ->
            if (state.isLoading || state.isNextPageLoading || state.cooldownSeconds > 0) return@update state
            if (state.totalCount in 1..serverOffset) return@update state
            shouldFetch = true
            currentFilter = state.filter
            currentSearchQuery = state.searchQuery
            state.copy(isNextPageLoading = true)
        }

        if (!shouldFetch) return

        viewModelScope.launch {
            getLaunchesUseCase(
                filter = currentFilter,
                searchQuery = currentSearchQuery,
                offset = serverOffset,
                isInitialPage = false
            ).onSuccess { result ->
                serverOffset += result.fetchedCount
                _uiState.update {
                    it.copy(
                        isNextPageLoading = false,
                        items = (it.items + result.items).distinctBy { item -> item.id },
                        totalCount = result.totalCount
                    )
                }
            }.onFailure { throwable ->
                _uiState.update { it.copy(isNextPageLoading = false) }
                handleError(throwable)
            }
        }
    }

    fun onFilterChanged(newFilter: LaunchFilter) {
        _uiState.update { it.copy(filter = newFilter) }
        loadLaunches()
    }

    fun resetFilters() {
        onFilterChanged(LaunchFilter())
    }

    fun openFilterSheet() {
        _uiState.update { it.copy(isFilterSheetOpen = true) }
    }

    fun closeFilterSheet() {
        _uiState.update { it.copy(isFilterSheetOpen = false) }
    }

    private fun handleError(throwable: Throwable) {
        val appError = throwable.toAppError()
        val errorResult = throwable.handleAppError()

        if (appError is AppError.RateLimit) {
            _uiState.update {
                it.copy(
                    isLoading = false,
                    cooldownSeconds = appError.cooldownSeconds,
                    initialCooldownSeconds = appError.cooldownSeconds,
                    error = errorResult.errorMessage,
                )
            }
            startCooldownTimer(appError.cooldownSeconds)
        } else {
            _uiState.update {
                it.copy(
                    isLoading = false,
                    error = errorResult.errorMessage,
                )
            }
        }
    }

    private fun startCooldownTimer(seconds: Int) {
        cooldownJob?.cancel()
        cooldownJob = viewModelScope.launch {
            var timeLeft = seconds
            while (timeLeft > 0) {
                delay(1000L.milliseconds)
                timeLeft--
                _uiState.update { it.copy(cooldownSeconds = timeLeft) }
            }
            _uiState.update { it.copy(error = null) }
        }
    }
}