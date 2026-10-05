package com.example.listanddetails.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.listanddetails.domain.repository.LaunchRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.time.Instant
import java.time.temporal.ChronoUnit
import kotlin.time.Duration.Companion.milliseconds

private const val INITIAL_PAGE_LIMIT = 15
private const val NEXT_PAGE_LIMIT = 20
private const val VIDEO_FETCH_LIMIT = 50
private const val DEFAULT_COOLDOWN_SECONDS = 30
private const val HTTP_TOO_MANY_REQUESTS = 429
private const val SEARCH_DEBOUNCE_MS = 500L

class LaunchListViewModel(
    private val repository: LaunchRepository
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

            val (netGte, netLte) = buildDateRange(currentFilter)
            val searchQuery = buildSearchQuery(currentFilter)
            val fetchLimit = if (currentFilter.hasVideoOnly) VIDEO_FETCH_LIMIT else INITIAL_PAGE_LIMIT

            repository.getListOfLaunches(
                search = searchQuery,
                ordering = currentFilter.sortOrder.apiValue,
                netLte = netLte,
                netGte = netGte,
                statusId = currentFilter.statusId,
                limit = fetchLimit,
                offset = 0
            ).onSuccess { result ->
                serverOffset = result.items.size
                val finalItems = if (currentFilter.hasVideoOnly) {
                    result.items.filter { it.hasVideo }
                } else {
                    result.items
                }

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        items = finalItems,
                        totalCount = result.totalCount
                    )
                }
            }.onFailure { throwable ->
                handleError(throwable)
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
            loadLaunches()
        }
    }

    fun loadNextPage() {
        val state = _uiState.value
        if (state.isLoading || state.isNextPageLoading || state.cooldownSeconds > 0) return
        if (state.totalCount in 1..serverOffset) return

        val currentFilter = state.filter

        viewModelScope.launch {
            _uiState.update { it.copy(isNextPageLoading = true) }

            val (netGte, netLte) = buildDateRange(currentFilter)
            val searchQuery = buildSearchQuery(currentFilter)
            val fetchLimit = if (currentFilter.hasVideoOnly) VIDEO_FETCH_LIMIT else NEXT_PAGE_LIMIT

            repository.getListOfLaunches(
                search = searchQuery,
                ordering = currentFilter.sortOrder.apiValue,
                netLte = netLte,
                netGte = netGte,
                statusId = currentFilter.statusId,
                limit = fetchLimit,
                offset = serverOffset
            ).onSuccess { result ->
                serverOffset += result.items.size
                val newItems = if (currentFilter.hasVideoOnly) {
                    result.items.filter { it.hasVideo }
                } else {
                    result.items
                }

                _uiState.update {
                    it.copy(
                        isNextPageLoading = false,
                        items = it.items + newItems,
                        totalCount = result.totalCount
                    )
                }
            }.onFailure {
                _uiState.update { it.copy(isNextPageLoading = false) }
            }
        }
    }

    fun onFilterChanged(newFilter: LaunchFilter) {
        _uiState.update { it.copy(filter = newFilter, items = emptyList()) }
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

    private fun buildDateRange(filter: LaunchFilter): Pair<String?, String?> {
        val nowIso = Instant.now().truncatedTo(ChronoUnit.SECONDS).toString()
        return when {
            filter.year != null -> {
                Pair("${filter.year}-01-01T00:00:00Z", "${filter.year}-12-31T23:59:59Z")
            }
            filter.hasVideoOnly -> {
                Pair(null, nowIso)
            }
            else -> {
                Pair(null, null)
            }
        }
    }

    private fun buildSearchQuery(filter: LaunchFilter): String? {
        val freeText = _uiState.value.searchQuery.takeIf { it.isNotBlank() }
        return listOfNotNull(filter.agency, filter.pad, freeText)
            .joinToString(" ")
            .takeIf { it.isNotBlank() }
    }

    private fun handleError(throwable: Throwable) {
        if (throwable is HttpException && throwable.code() == HTTP_TOO_MANY_REQUESTS) {
            val retryAfterHeader = throwable.response()?.headers()?.get("Retry-After")?.toIntOrNull()
            val secondsToWait = retryAfterHeader ?: DEFAULT_COOLDOWN_SECONDS

            _uiState.update {
                it.copy(
                    isLoading = false,
                    cooldownSeconds = secondsToWait,
                    initialCooldownSeconds = secondsToWait
                )
            }
            startCooldownTimer(secondsToWait)
        } else {
            _uiState.update {
                it.copy(isLoading = false, error = throwable.message)
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