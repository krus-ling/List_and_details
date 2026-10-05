package com.example.listanddetails.ui.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.listanddetails.ui.list.components.ActiveFilterChipsRow
import com.example.listanddetails.ui.list.components.EmptyLaunchesView
import com.example.listanddetails.ui.list.components.FilterBottomSheet
import com.example.listanddetails.ui.list.components.LaunchListErrorView
import com.example.listanddetails.ui.list.components.LaunchListSkeleton
import com.example.listanddetails.ui.list.components.LaunchListTopBar
import com.example.listanddetails.ui.list.components.LaunchesLazyColumn
import com.example.listanddetails.ui.list.components.mockPreviewLaunches
import org.koin.androidx.compose.koinViewModel

@Composable
fun LaunchListScreen(
    onLaunchClick: (String) -> Unit,
    viewModel: LaunchListViewModel = koinViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchListContent(
        state = state,
        onLaunchClick = onLaunchClick,
        onRetry = viewModel::loadLaunches,
        onSearchQueryChange = viewModel::onSearchQueryChanged,
        onLoadNextPage = viewModel::loadNextPage,
        onOpenFilter = viewModel::openFilterSheet,
        onDismissFilter = viewModel::closeFilterSheet,
        onFilterChanged = viewModel::onFilterChanged,
        onResetFilter = viewModel::resetFilters
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LaunchListContent(
    state: LaunchListUiState,
    onLaunchClick: (String) -> Unit,
    onRetry: () -> Unit,
    onSearchQueryChange: (String) -> Unit = {},
    onLoadNextPage: () -> Unit = {},
    onOpenFilter: () -> Unit,
    onDismissFilter: () -> Unit,
    onFilterChanged: (LaunchFilter) -> Unit,
    onResetFilter: () -> Unit
) {
    Scaffold(
        topBar = {
            LaunchListTopBar(
                searchQuery = state.searchQuery,
                onSearchQueryChange = onSearchQueryChange,
                isLoading = state.isLoading,
                hasItems = state.items.isNotEmpty()
            )
        },
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { innerPadding ->

        when {
            state.isLoading && state.items.isEmpty() -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    if (state.filter.isActive) {
                        ActiveFilterChipsRow(
                            filter = state.filter,
                            onFilterChanged = onFilterChanged,
                            onResetFilter = onResetFilter
                        )
                    }
                    LaunchListSkeleton()
                }
            }

            state.error != null && state.items.isEmpty() -> {
                LaunchListErrorView(
                    error = state.error,
                    cooldownSeconds = state.cooldownSeconds,
                    initialCooldownSeconds = state.initialCooldownSeconds,
                    onRetry = onRetry,
                    modifier = Modifier.padding(innerPadding)
                )
            }

            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    if (state.filter.isActive) {
                        ActiveFilterChipsRow(
                            filter = state.filter,
                            onFilterChanged = onFilterChanged,
                            onResetFilter = onResetFilter
                        )
                    }

                    if (state.items.isEmpty()) {
                        EmptyLaunchesView()
                    } else {
                        LaunchesLazyColumn(
                            items = state.items,
                            totalCount = state.totalCount,
                            filter = state.filter,
                            isLoading = state.isLoading,
                            isNextPageLoading = state.isNextPageLoading,
                            onLaunchClick = onLaunchClick,
                            onLoadNextPage = onLoadNextPage,
                            onOpenFilter = onOpenFilter
                        )
                    }
                }
            }
        }

        if (state.isFilterSheetOpen) {
            FilterBottomSheet(
                filter = state.filter,
                onDismiss = onDismissFilter,
                onApply = { newFilter ->
                    onFilterChanged(newFilter)
                    onDismissFilter()
                },
                onReset = {
                    onResetFilter()
                    onDismissFilter()
                }
            )
        }
    }
}

@Preview(name = "Скелетон списка", showBackground = true)
@Composable
private fun LaunchListSkeletonPreview() {
    MaterialTheme {
        LaunchListSkeleton()
    }
}

@Preview(name = "1. Список с данными", showBackground = true, showSystemUi = true)
@Composable
private fun LaunchListPreview() {
    MaterialTheme {
        LaunchListContent(
            state = LaunchListUiState(items = mockPreviewLaunches),
            onLaunchClick = {},
            onRetry = {},
            onOpenFilter = {},
            onDismissFilter = {},
            onFilterChanged = {},
            onResetFilter = {}
        )
    }
}

@Preview(name = "2. Список с активными фильтрами", showBackground = true, showSystemUi = true)
@Composable
private fun LaunchListWithActiveFiltersPreview() {
    MaterialTheme {
        LaunchListContent(
            state = LaunchListUiState(
                items = mockPreviewLaunches.take(2),
                filter = LaunchFilter(
                    agency = "SpaceX",
                    statusId = 3,
                    hasVideoOnly = true
                )
            ),
            onLaunchClick = {},
            onRetry = {},
            onOpenFilter = {},
            onDismissFilter = {},
            onFilterChanged = {},
            onResetFilter = {}
        )
    }
}

@Preview(name = "3. Открытая шторка фильтров", showBackground = true)
@Composable
private fun FilterBottomSheetPreview() {
    MaterialTheme {
        Surface {
            FilterBottomSheet(
                filter = LaunchFilter(agency = "SpaceX"),
                onDismiss = {},
                onApply = {},
                onReset = {}
            )
        }
    }
}

@Preview(name = "4. Загрузка", showBackground = true)
@Composable
private fun LaunchListLoadingPreview() {
    MaterialTheme {
        LaunchListContent(
            state = LaunchListUiState(isLoading = true),
            onLaunchClick = {},
            onRetry = {},
            onOpenFilter = {},
            onDismissFilter = {},
            onFilterChanged = {},
            onResetFilter = {}
        )
    }
}

@Preview(name = "5. Ошибка", showBackground = true)
@Composable
private fun LaunchListErrorPreview() {
    MaterialTheme {
        LaunchListContent(
            state = LaunchListUiState(error = "Сервер временно недоступен"),
            onLaunchClick = {},
            onRetry = {},
            onOpenFilter = {},
            onDismissFilter = {},
            onFilterChanged = {},
            onResetFilter = {}
        )
    }
}