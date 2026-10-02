package com.example.listanddetails.ui.details

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import androidx.core.net.toUri
import com.example.listanddetails.domain.model.LaunchDetail

@Composable
fun LaunchDetailScreen(
    launchId: String,
    onBackClick: () -> Unit,
    viewModel: LaunchDetailViewModel = koinViewModel(
        key = launchId,
        parameters = { parametersOf(launchId) }
    )
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchDetailContent(
        state = state,
        onBackClick = onBackClick,
        onRetry = viewModel::loadLaunch,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LaunchDetailContent(
    state: LaunchDetailUiState,
    onBackClick: () -> Unit,
    onRetry: () -> Unit,
) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(state.launch?.name ?: "Детали запуска") },
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Назад"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->

        when {
            state.isLoading && state.launch == null -> {

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            state.error != null && state.launch == null -> {

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
                state.launch?.let { launch ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        if (launch.imageUrl != null) {
                            // Главное фото запуска
                            AsyncImage(
                                model = launch.imageUrl,
                                contentDescription = launch.name,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(240.dp)
                            )
                        }

                        // Блоки информации с отступами
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            // Название, агентство и статус
                            Text(
                                text = launch.name,
                                style = MaterialTheme.typography.titleMedium,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = launch.agency,
                                        style = MaterialTheme.typography.titleMedium,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Text(
                                        text = launch.date,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }

                                LaunchStatusBadge(status = launch.status)
                            }
                        }

                        // Баннер сбоя при наличии failReason
                        val isFailure = launch.status.contains("Fail", ignoreCase = true)
                        if (isFailure) {
                            Card(
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.errorContainer
                                ),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(
                                        text = "Сбой запуска",
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onErrorContainer
                                    )
                                    Text(
                                        text = if (!launch.failReason.isNullOrBlank()) {
                                            launch.failReason
                                        } else {
                                            "Запуск завершился неудачей. Подробные данные о причинах сбоя в архиве отсутствуют."
                                        },
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onErrorContainer,
                                        modifier = Modifier.padding(top = 4.dp)
                                    )
                                }
                            }
                        }

                        // Карточка миссия
                        ElevatedCard(modifier = Modifier.fillMaxWidth()) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = "Миссия: ${launch.missionName}",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.SemiBold
                                )

                                if (!launch.missionType.isNullOrBlank()) {
                                    Text(
                                        text = "Тип: ${launch.missionType}",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }

                                if (!launch.orbit.isNullOrBlank()) {
                                    Text(
                                        text = "Орбита: ${launch.orbit}",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }

                                Text(
                                    text = launch.missionDescription ?: "Описание отсутствует",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        // Карточка ракета
                        ElevatedCard(modifier = Modifier.fillMaxWidth()) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Text(
                                    text = "Ракета: ${launch.rocketName}",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.SemiBold
                                )

                                if (launch.rocketImage != null) {
                                    AsyncImage(
                                        model = launch.rocketImage,
                                        contentDescription = launch.rocketName,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(180.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                    )
                                }
                            }
                        }

                        // Карточка стартовый комплекс
                        ElevatedCard(modifier = Modifier.fillMaxWidth()) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = "Стартовый комплекс",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = "Площадка: ${launch.padName ?: "Не указана"}",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    text = "Локация: ${launch.location ?: "Не указана"}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        // Кнопка трансляции
                        if (!launch.videoUrl.isNullOrBlank()) {
                            Button(
                                onClick = {
                                    val intent =
                                        Intent(Intent.ACTION_VIEW, launch.videoUrl.toUri())
                                    context.startActivity(intent)
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp)
                            ) {
                                Text("Смотреть трансляцию")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LaunchStatusBadge(status: String) {
    val isSuccess = status.contains("Success", ignoreCase = true)
    val isFailure = status.contains("Fail", ignoreCase = true)

    val containerColor = when {
        isSuccess -> Color(0xFF2E7D32).copy(alpha = 0.15f)
        isFailure -> MaterialTheme.colorScheme.errorContainer
        else -> MaterialTheme.colorScheme.surfaceVariant
    }

    val labelColor = when {
        isSuccess -> Color(0xFF1B5E20)
        isFailure -> MaterialTheme.colorScheme.onErrorContainer
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }

    SuggestionChip(
        onClick = {},
        label = {
            Text(
                text = status,
                fontWeight = FontWeight.Medium,
                color = labelColor
            )
        },
        colors = SuggestionChipDefaults.suggestionChipColors(
            containerColor = containerColor
        ),
        border = null
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun LaunchDetailContentPreview() {
    MaterialTheme {
        LaunchDetailContent(
            state = LaunchDetailUiState(launch = mockLaunchDetail),
            onBackClick = {},
            onRetry = {}
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun LaunchDetailLoadingPreview() {
    MaterialTheme {
        LaunchDetailContent(
            state = LaunchDetailUiState(isLoading = true),
            onBackClick = {},
            onRetry = {}
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun LaunchDetailErrorPreview() {
    MaterialTheme {
        LaunchDetailContent(
            state = LaunchDetailUiState(error = "Не удалось загрузить данные о запуске"),
            onBackClick = {},
            onRetry = {}
        )
    }
}

private val mockLaunchDetail = LaunchDetail(
    id = "9a50023f-07fe-45ac-9ac6-7fb6b5ea970d",
    name = "Falcon 1 | DemoSat",
    status = "Launch Failure",
    date = "21 марта 2007, 01:10",
    agency = "SpaceX",
    imageUrl = "https://thespacedevs-prod.nyc3.digitaloceanspaces.com/media/images/falcon_image_20190222030438.jpeg",
    rocketName = "Falcon 1",
    rocketImage = "https://thespacedevs-prod.nyc3.digitaloceanspaces.com/media/images/falcon_image_20190222030438.jpeg",
    missionName = "DemoSat",
    missionDescription = "Second test flight of Falcon 1. Only a mass simulator was flown.",
    padName = "Omelek Island",
    location = "Ronald Reagan Ballistic Missile Defense Test Site, Kwajalein Atoll, Marshall Islands",
    orbit = "Low Earth Orbit",
    missionType = "Test Flight",
    failReason = "Premature engine shutdown at T+7:30",
    videoUrl = "https://www.youtube.com/watch?v=f9FVOKtRPAE",
    rawDate = "2007-03-21T01:10:00Z"
)