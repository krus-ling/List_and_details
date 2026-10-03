package com.example.listanddetails.ui.list

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.listanddetails.R
import com.example.listanddetails.ui.list.components.ActiveFilterChipsRow
import com.example.listanddetails.ui.list.components.FilterBottomSheet
import com.example.listanddetails.ui.list.components.LaunchCard
import com.example.listanddetails.ui.list.components.LaunchListErrorView
import com.example.listanddetails.ui.list.components.LaunchListSkeleton
import com.example.listanddetails.ui.list.components.LaunchListTopBar
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
    onLoadNextPage: () -> Unit = {},
    onOpenFilter: () -> Unit,
    onDismissFilter: () -> Unit,
    onFilterChanged: (LaunchFilter) -> Unit,
    onResetFilter: () -> Unit
) {
    val listAlpha by animateFloatAsState(
        targetValue = if (state.isLoading && state.items.isNotEmpty()) 0.5f else 1.0f,
        label = "listAlpha"
    )

    Scaffold(
        topBar = {
            LaunchListTopBar(
                filter = state.filter,
                isLoading = state.isLoading,
                hasItems = state.items.isNotEmpty(),
                onOpenFilter = onOpenFilter
            )
        }
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
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = stringResource(R.string.no_launches_found),
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    } else {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .alpha(listAlpha)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 6.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = stringResource(R.string.launches_found, state.totalCount),
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            val listState = rememberLazyListState()
                            val shouldLoadMore = remember {
                                derivedStateOf {
                                    val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()
                                    lastVisibleItem != null && lastVisibleItem.index >= listState.layoutInfo.totalItemsCount - 3
                                }
                            }

                            LaunchedEffect(shouldLoadMore.value) {
                                if (shouldLoadMore.value) {
                                    onLoadNextPage()
                                }
                            }

                            LazyColumn(
                                state = listState,
                                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp, top = 4.dp),
                                verticalArrangement = Arrangement.spacedBy(16.dp),
                                modifier = Modifier.fillMaxSize()
                            ) {
                                items(
                                    items = state.items,
                                    key = { it.id }
                                ) { launch ->
                                    LaunchCard(
                                        launch = launch,
                                        onClick = { onLaunchClick(launch.id) }
                                    )
                                }

                                if (state.isNextPageLoading) {
                                    item {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(16.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            CircularProgressIndicator()
                                        }
                                    }
                                }
                            }
                        }
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