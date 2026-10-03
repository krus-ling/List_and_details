package com.example.listanddetails.ui.details

import android.annotation.SuppressLint
import android.content.Intent
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.example.listanddetails.R
import com.example.listanddetails.domain.model.LaunchDetail
import com.example.listanddetails.ui.theme.LiveIndicatorGreen
import com.example.listanddetails.ui.theme.OnSuccessGreen
import com.example.listanddetails.ui.theme.SuccessGreen
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import java.time.Instant
import kotlin.time.Duration.Companion.milliseconds

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
    val backDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

    LaunchDetailContent(
        state = state,
        onBackClick = {
            if (backDispatcher != null) {
                backDispatcher.onBackPressed()
            } else {
                onBackClick()
            }
        },
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
                    CircularProgressIndicator()
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

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(headerHeight)
                            .graphicsLayer {
                                translationY = -scrollState.value * 0.45f
                            }
                    ) {
                        val headerImage = launch.imageUrl ?: launch.rocketImage
                        if (headerImage != null) {
                            AsyncImage(
                                model = headerImage,
                                contentDescription = launch.name,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clickable {
                                        viewerImageUrl = headerImage
                                        viewerImageTitle = launch.name
                                    }
                            )
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(
                                            Color.Black.copy(alpha = 0.4f),
                                            Color.Transparent,
                                            Color.Black.copy(alpha = 0.85f)
                                        )
                                    )
                                )
                        )

                        Column(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(start = 20.dp, end = 20.dp, bottom = 44.dp)
                        ) {
                            TMinusCountdownBadge(rawDate = launch.rawDate)

                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = launch.name,
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

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

                                // Баннер сбоя при наличии failReason
                                if (launch.status.contains("Fail", ignoreCase = true)) {
                                    Card(
                                        colors = CardDefaults.cardColors(
                                            containerColor = MaterialTheme.colorScheme.errorContainer
                                        ),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Column(modifier = Modifier.padding(16.dp)) {
                                            Text(
                                                text = stringResource(R.string.launch_failure),
                                                style = MaterialTheme.typography.titleSmall,
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.onErrorContainer
                                            )

                                            val failReasonText = if (state.isTranslated && !state.translatedFailReason.isNullOrBlank()) {
                                                state.translatedFailReason
                                            } else {
                                                launch.failReason?.takeIf { it.isNotBlank() }
                                                    ?: stringResource(R.string.failure_reason_missing)
                                            }

                                            Text(
                                                text = failReasonText,
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = MaterialTheme.colorScheme.onErrorContainer,
                                                modifier = Modifier.padding(top = 4.dp)
                                            )
                                        }
                                    }
                                }

                                // Карточка миссии с кнопкой перевода
                                ElevatedCard(modifier = Modifier.fillMaxWidth()) {
                                    Column(
                                        modifier = Modifier.padding(16.dp),
                                        verticalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = stringResource(R.string.mission_prefix, launch.missionName),
                                                style = MaterialTheme.typography.titleMedium,
                                                fontWeight = FontWeight.SemiBold,
                                                modifier = Modifier.weight(1f)
                                            )

                                            // Кнопка перевода
                                            if (!launch.missionDescription.isNullOrBlank()) {
                                                TextButton(
                                                    onClick = onTranslateClick,
                                                    enabled = !state.isTranslating
                                                ) {
                                                    if (state.isTranslating) {
                                                        CircularProgressIndicator(
                                                            modifier = Modifier.size(16.dp),
                                                            strokeWidth = 2.dp
                                                        )
                                                    } else {
                                                        Text(
                                                            text = if (state.isTranslated) "Оригинал" else "Перевести",
                                                            style = MaterialTheme.typography.labelLarge
                                                        )
                                                    }
                                                }
                                            }
                                        }

                                        val missionTypeText = if (state.isTranslated && !state.translatedMissionType.isNullOrBlank()) {
                                            state.translatedMissionType
                                        } else {
                                            launch.missionType
                                        }
                                        if (!missionTypeText.isNullOrBlank()) {
                                            Text(
                                                text = stringResource(R.string.mission_type_prefix, missionTypeText),
                                                style = MaterialTheme.typography.bodyMedium
                                            )
                                        }

                                        val orbitText = if (state.isTranslated && !state.translatedOrbit.isNullOrBlank()) {
                                            state.translatedOrbit
                                        } else {
                                            launch.orbit
                                        }
                                        if (!orbitText.isNullOrBlank()) {
                                            Text(
                                                text = stringResource(R.string.orbit_prefix, orbitText),
                                                style = MaterialTheme.typography.bodyMedium
                                            )
                                        }

                                        val descriptionText = if (state.isTranslated && !state.translatedDescription.isNullOrBlank()) {
                                            state.translatedDescription
                                        } else {
                                            launch.missionDescription ?: stringResource(R.string.not_specified)
                                        }

                                        Text(
                                            text = descriptionText,
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }

                                ElevatedCard(modifier = Modifier.fillMaxWidth()) {
                                    Column(
                                        modifier = Modifier.padding(16.dp),
                                        verticalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        Text(
                                            text = stringResource(R.string.rocket_prefix, launch.rocketName),
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
                                                    .clip(RoundedCornerShape(12.dp))
                                                    .clickable {
                                                        viewerImageUrl = launch.rocketImage
                                                        viewerImageTitle = launch.rocketName
                                                    }
                                            )
                                        }
                                    }
                                }

                                ElevatedCard(modifier = Modifier.fillMaxWidth()) {
                                    Column(
                                        modifier = Modifier.padding(16.dp),
                                        verticalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Text(
                                            text = stringResource(R.string.launch_pad_complex),
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                        Text(
                                            text = stringResource(R.string.pad_prefix, launch.padName ?: stringResource(R.string.not_specified)),
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                        Text(
                                            text = stringResource(R.string.location_prefix, launch.location ?: stringResource(R.string.not_specified)),
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }

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

@Composable
private fun FullScreenImageViewer(
    imageUrl: String,
    title: String?,
    onDismiss: () -> Unit
) {
    var scale by rememberSaveable { mutableFloatStateOf(1f) }
    var offsetX by rememberSaveable { mutableFloatStateOf(0f) }
    var offsetY by rememberSaveable { mutableFloatStateOf(0f) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onDoubleTap = {
                                if (scale > 1f) {
                                    scale = 1f
                                    offsetX = 0f
                                    offsetY = 0f
                                } else {
                                    scale = 2.5f
                                }
                            }
                        )
                    }
                    .pointerInput(Unit) {
                        detectTransformGestures { _, pan, zoom, _ ->
                            scale = (scale * zoom).coerceIn(1f, 5f)
                            if (scale == 1f) {
                                offsetX = 0f
                                offsetY = 0f
                            } else {
                                val extraWidth = (size.width * scale - size.width) / 2
                                val extraHeight = (size.height * scale - size.height) / 2
                                val maxX = extraWidth.coerceAtLeast(0f)
                                val maxY = extraHeight.coerceAtLeast(0f)

                                offsetX = (offsetX + pan.x).coerceIn(-maxX, maxX)
                                offsetY = (offsetY + pan.y).coerceIn(-maxY, maxY)
                            }
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = title,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .graphicsLayer(
                            scaleX = scale,
                            scaleY = scale,
                            translationX = offsetX,
                            translationY = offsetY
                        )
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (!title.isNullOrBlank()) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )
                } else {
                    Spacer(modifier = Modifier.weight(1f))
                }

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.25f))
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(R.string.close),
                        tint = Color.White
                    )
                }
            }
        }
    }
}

@SuppressLint("DefaultLocale")
@Composable
private fun TMinusCountdownBadge(rawDate: String?) {
    var currentTime by remember { mutableLongStateOf(System.currentTimeMillis()) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(1000L.milliseconds)
            currentTime = System.currentTimeMillis()
        }
    }

    val targetMillis = remember(rawDate) {
        try {
            rawDate?.let { Instant.parse(it).toEpochMilli() } ?: 0L
        } catch (e: Exception) {
            0L
        }
    }

    val diffSeconds = (targetMillis - currentTime) / 1000

    if (diffSeconds > 0) {
        val days = diffSeconds / 86400
        val hours = (diffSeconds % 86400) / 3600
        val minutes = (diffSeconds % 3600) / 60
        val seconds = diffSeconds % 60

        Surface(
            color = Color.Black.copy(alpha = 0.65f),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.padding(bottom = 4.dp)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(LiveIndicatorGreen)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = String.format("T - %02dd %02dh %02dm %02ds", days, hours, minutes, seconds),
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = LiveIndicatorGreen
                )
            }
        }
    }
}

@Composable
private fun LaunchStatusBadge(status: String) {
    val isSuccess = status.contains("Success", ignoreCase = true)
    val isFailure = status.contains("Fail", ignoreCase = true)

    val containerColor = when {
        isSuccess -> SuccessGreen.copy(alpha = 0.15f)
        isFailure -> MaterialTheme.colorScheme.errorContainer
        else -> MaterialTheme.colorScheme.surfaceVariant
    }

    val labelColor = when {
        isSuccess -> OnSuccessGreen
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
            onRetry = {},
            onTranslateClick = {}
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
            onRetry = {},
            onTranslateClick = {}
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