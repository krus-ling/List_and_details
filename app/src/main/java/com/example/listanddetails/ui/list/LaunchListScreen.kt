package com.example.listanddetails.ui.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.listanddetails.domain.model.LaunchItem
import com.example.listanddetails.ui.list.components.LaunchCard
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
        onRetry = { viewModel.loadLaunches() },
        onFilterSelect = { filter -> viewModel.loadLaunches(filter) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LaunchListContent(
    state: LaunchListUiState,
    onLaunchClick: (String) -> Unit,
    onRetry: () -> Unit,
    onFilterSelect: (String?) -> Unit
) {
    Scaffold(
        topBar =  {
            TopAppBar(
                title = { Text("Космические запуски") }
            )
        }

    ) { innerPadding ->

        when {
            state.isLoading && state.items.isEmpty() -> {

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            state.error != null && state.items.isEmpty() -> {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = state.error)
                    Button(onClick = onRetry) {
                        Text("Повторить")
                    }
                }
            }

            else -> {

                val filters = listOf(
                    null to "Все",
                    "SpaceX" to "SpaceX",
                    "NASA" to "NASA",
                    "Roscosmos" to "Роскосмос",
                    "Failure" to "Аварийные"
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(filters) { (filterQuery, title) ->
                            val isSelected = state.selectedFilter == filterQuery
                            FilterChip(
                                selected = isSelected,
                                onClick = { onFilterSelect(filterQuery) },
                                label = { Text(title) }
                            )
                        }
                    }

                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.padding(innerPadding)
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
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LaunchListLoadingPreview() {
    // Мгновенно смотрим, как выглядит лоадер
    LaunchListContent(
        state = LaunchListUiState(isLoading = true),
        onLaunchClick = {},
        onRetry = {},
        onFilterSelect = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun LaunchListErrorPreview() {
    // Мгновенно смотрим экран с ошибкой
    LaunchListContent(
        state = LaunchListUiState(error = "Сервер недоступен"),
        onLaunchClick = {},
        onRetry = {},
        onFilterSelect = {}
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun LaunchListPreview() {
    LaunchListContent(
        state = LaunchListUiState(items = mockPreviewLaunches),
        onLaunchClick = {},
        onRetry = {},
        onFilterSelect = {}
    )
}

private val mockPreviewLaunches = listOf(
    LaunchItem(
        id = "1",
        name = "Falcon 1 | DemoSat",
        status = "Launch Failure",
        date = "21 марта 2007, 01:10",
        agency = "SpaceX",
        imageUrl = "https://thespacedevs-prod.nyc3.digitaloceanspaces.com/media/images/falcon_image_20190222030438.jpeg"
    ),
    LaunchItem(
        id = "2",
        name = "Falcon 9 Block 5 | Starlink Group 6-58 (Direct to Cell)",
        status = "Success",
        date = "15 мая 2024, 18:30",
        agency = "SpaceX",
        imageUrl = "https://thespacedevs-prod.nyc3.digitaloceanspaces.com/media/images/falcon_9_image_20230807133459.jpeg"
    ),
    LaunchItem(
        id = "3",
        name = "Sputnik 8A91 | D-1 1",
        status = "Launch Failure",
        date = "27 апреля 1958, 07:00",
        agency = "Soviet Space Program",
        imageUrl = null
    ),
    LaunchItem(
        id = "4",
        name = "Vanguard | Vanguard TV-3BU",
        status = "Launch Failure",
        date = "5 февраля 1958, 07:33",
        agency = "US Navy",
        imageUrl = null
    ),
    LaunchItem(
        id = "5",
        name = "Electron | There and Back Again",
        status = "Success",
        date = "2 мая 2022, 22:49",
        agency = "Rocket Lab",
        imageUrl = null
    ),
    LaunchItem(
        id = "6",
        name = "Atlas V 551 | Project Kuiper Protoflight",
        status = "Success",
        date = "6 октября 2023, 18:06",
        agency = "United Launch Alliance",
        imageUrl = null
    ),
    LaunchItem(
        id = "7",
        name = "Ariane 5 ECA | James Webb Space Telescope",
        status = "Success",
        date = "25 декабря 2021, 12:20",
        agency = "Arianespace",
        imageUrl = null
    ),
    LaunchItem(
        id = "8",
        name = "Starship | Integrated Flight Test 3",
        status = "Partial Failure",
        date = "14 марта 2024, 13:25",
        agency = "SpaceX",
        imageUrl = null
    )
)