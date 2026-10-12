package com.example.listanddetails.ui.agencies

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.listanddetails.R
import com.example.listanddetails.domain.model.AgencyItem
import com.example.listanddetails.ui.agencies.components.AgencyCard
import com.example.listanddetails.ui.agencies.components.AgencyDetailDialog
import com.example.listanddetails.ui.components.RocketLoadingIndicator
import com.example.listanddetails.ui.components.SearchBarTopBar
import org.koin.androidx.compose.koinViewModel

@Composable
fun AgencyListScreen(
    viewModel: AgencyListViewModel = koinViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    AgencyListContent(
        state = state,
        onRetry = viewModel::loadAgencies,
        onSearchQueryChange = viewModel::onSearchQueryChanged,
        onLoadNextPage = viewModel::loadNextPage
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgencyListContent(
    state: AgencyListUiState,
    onRetry: () -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onLoadNextPage: () -> Unit
) {
    var selectedAgency by remember { mutableStateOf<AgencyItem?>(null) }

    Scaffold(
        topBar = {
            SearchBarTopBar(
                title = stringResource(R.string.agencies_title),
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
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    RocketLoadingIndicator()
                }
            }

            state.error != null && state.items.isEmpty() -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = state.error,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.error
                    )
                    Button(
                        onClick = onRetry,
                        modifier = Modifier.padding(top = 16.dp)
                    ) {
                        Text(stringResource(R.string.retry))
                    }
                }
            }

            else -> {
                if (state.items.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.no_agencies_found),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = stringResource(R.string.agencies_found, state.totalCount),
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
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(
                                items = state.items,
                                key = { it.id }
                            ) { agency ->
                                AgencyCard(
                                    agency = agency,
                                    onClick = { selectedAgency = agency }
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

        selectedAgency?.let { agency ->
            AgencyDetailDialog(
                agency = agency,
                onDismiss = { selectedAgency = null }
            )
        }
    }
}