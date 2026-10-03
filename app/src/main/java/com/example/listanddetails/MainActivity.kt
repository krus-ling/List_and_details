package com.example.listanddetails

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.example.listanddetails.ui.details.LaunchDetailScreen
import com.example.listanddetails.ui.list.LaunchListScreen
import com.example.listanddetails.ui.navigation.Route
import com.example.listanddetails.ui.theme.ListAndDetailsTheme
import kotlinx.serialization.json.Json

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ListAndDetailsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val backStack = rememberSaveable(
                        saver = listSaver(
                            save = { list -> list.map { Json.encodeToString(Route.serializer(), it) } },
                            restore = { saved -> saved.map { Json.decodeFromString(Route.serializer(), it) }.toMutableStateList() }
                        )
                    ) {
                        mutableStateListOf<Route>(Route.LaunchList)
                    }

                    BackHandler(enabled = backStack.size > 1) {
                        backStack.removeAt(backStack.lastIndex)
                    }

                    NavDisplay(
                        backStack = backStack,
                        onBack = {
                            if (backStack.size > 1) {
                                backStack.removeAt(backStack.lastIndex)
                            } else {
                                finish()
                            }
                        },
                        transitionSpec = {
                            val isPush = initialState.key is Route.LaunchList && targetState.key is Route.LaunchDetails
                            val isPop = initialState.key is Route.LaunchDetails && targetState.key is Route.LaunchList
                            val duration = 350
                            val motionEasing = CubicBezierEasing(0.05f, 0.7f, 0.1f, 1.0f)

                            if (isPush) {
                                slideInHorizontally(
                                    animationSpec = tween(duration, easing = motionEasing)
                                ) { fullWidth -> fullWidth }.togetherWith(
                                    slideOutHorizontally(
                                        animationSpec = tween(duration, easing = motionEasing)
                                    ) { fullWidth -> -fullWidth / 4 } + fadeOut(
                                        animationSpec = tween(duration, easing = motionEasing),
                                        targetAlpha = 0.5f
                                    )
                                ).apply {
                                    targetContentZIndex = 1f
                                }
                            } else if (isPop) {
                                (slideInHorizontally(
                                    animationSpec = tween(duration, easing = motionEasing)
                                ) { fullWidth -> -fullWidth / 4 } + fadeIn(
                                    animationSpec = tween(duration, easing = motionEasing),
                                    initialAlpha = 0.5f
                                )).togetherWith(
                                    slideOutHorizontally(
                                        animationSpec = tween(duration, easing = motionEasing)
                                    ) { fullWidth -> fullWidth }
                                ).apply {
                                    targetContentZIndex = -1f
                                }
                            } else {
                                fadeIn(animationSpec = tween(0)) togetherWith fadeOut(animationSpec = tween(0))
                            }
                        }
                    ) { key ->
                        when (key) {
                            is Route.LaunchList -> NavEntry(key) {
                                Surface(
                                    modifier = Modifier.fillMaxSize(),
                                    color = MaterialTheme.colorScheme.background
                                ) {
                                    LaunchListScreen(
                                        onLaunchClick = { launchId ->
                                            backStack.add(Route.LaunchDetails(launchId))
                                        }
                                    )
                                }
                            }

                            is Route.LaunchDetails -> NavEntry(key) {
                                Surface(
                                    modifier = Modifier.fillMaxSize(),
                                    color = MaterialTheme.colorScheme.background
                                ) {
                                    LaunchDetailScreen(
                                        launchId = key.id,
                                        onBackClick = {
                                            if (backStack.size > 1) {
                                                backStack.removeAt(backStack.lastIndex)
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}