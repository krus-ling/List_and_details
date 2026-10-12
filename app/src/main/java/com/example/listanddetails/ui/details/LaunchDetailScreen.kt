package com.example.listanddetails.ui.details

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.listanddetails.R
import com.example.listanddetails.domain.model.LaunchDetail
import com.example.listanddetails.ui.components.RocketLoadingIndicator
import com.example.listanddetails.ui.details.components.FullScreenImageViewer
import com.example.listanddetails.ui.details.components.LaunchDetailHeader
import com.example.listanddetails.ui.details.components.LaunchFailureCard
import com.example.listanddetails.ui.details.components.LaunchMissionCard
import com.example.listanddetails.ui.details.components.LaunchPadCard
import com.example.listanddetails.ui.details.components.LaunchRocketCard
import com.example.listanddetails.ui.details.components.LaunchStatusBadge
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

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
        onTranslateClick = viewModel::toggleTranslation
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LaunchDetailContent(
    state: LaunchDetailUiState,
    onBackClick: () -> Unit,
    onRetry: () -> Unit,
    onTranslateClick: () -> Unit
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val isHeaderCollapsed by remember {
        derivedStateOf { scrollState.value > 450 }
    }
    var viewerImageUrl by rememberSaveable { mutableStateOf<String?>(null) }
    var viewerImageTitle by rememberSaveable { mutableStateOf<String?>(null) }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        when {
            state.isLoading && state.launch == null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    RocketLoadingIndicator()
                }
            }

            state.error != null && state.launch == null -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = state.error, style = MaterialTheme.typography.bodyLarge)
                    Button(onClick = onRetry, modifier = Modifier.padding(top = 16.dp)) {
                        Text(stringResource(R.string.retry))
                    }
                }
            }

            else -> {
                state.launch?.let { launch ->
                    val headerHeight = 360.dp

                    LaunchDetailHeader(
                        launch = launch,
                        headerHeight = headerHeight,
                        scrollOffset = { scrollState.value },
                        onImageClick = { url, title ->
                            viewerImageUrl = url
                            viewerImageTitle = title
                        }
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(scrollState)
                    ) {
                        Spacer(modifier = Modifier.height(headerHeight - 24.dp))

                        Surface(
                            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
                            color = MaterialTheme.colorScheme.background,
                            shadowElevation = 8.dp,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .navigationBarsPadding()
                                    .padding(20.dp),
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(width = 40.dp, height = 4.dp)
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.outlineVariant)
                                        .align(Alignment.CenterHorizontally)
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
                                            fontWeight = FontWeight.Bold,
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

                                if (launch.status.contains("Fail", ignoreCase = true)) {
                                    LaunchFailureCard(launch = launch, state = state)
                                }

                                LaunchMissionCard(
                                    launch = launch,
                                    state = state,
                                    onTranslateClick = onTranslateClick
                                )

                                LaunchRocketCard(
                                    launch = launch,
                                    onImageClick = { url, title ->
                                        viewerImageUrl = url
                                        viewerImageTitle = title
                                    }
                                )

                                LaunchPadCard(launch = launch)

                                if (!launch.videoUrl.isNullOrBlank()) {
                                    Button(
                                        onClick = {
                                            val intent = Intent(Intent.ACTION_VIEW, launch.videoUrl.toUri())
                                            context.startActivity(intent)
                                        },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(52.dp)
                                    ) {
                                        Text(stringResource(R.string.watch_video), fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }

                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier
                            .statusBarsPadding()
                            .padding(12.dp)
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(Color.Black.copy(alpha = 0.45f))
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back),
                            tint = Color.White
                        )
                    }

                    AnimatedVisibility(
                        visible = isHeaderCollapsed,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        TopAppBar(
                            title = {
                                Text(
                                    text = launch.name,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            },
                            navigationIcon = {
                                IconButton(onClick = onBackClick) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = stringResource(R.string.back)
                                    )
                                }
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            )
                        )
                    }
                }
            }
        }

        if (viewerImageUrl != null) {
            FullScreenImageViewer(
                imageUrl = viewerImageUrl!!,
                title = viewerImageTitle,
                onDismiss = {
                    viewerImageUrl = null
                    viewerImageTitle = null
                }
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun LaunchDetailContentPreview() {
    MaterialTheme {
        LaunchDetailContent(
            state = LaunchDetailUiState(launch = mockLaunchDetail),
            onBackClick = {},
            onRetry = {},
            onTranslateClick = {}
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