package com.example.listanddetails.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.example.listanddetails.ui.details.LaunchDetailScreen

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    initialRoute: Route = Route.MainTab,
    onExitApp: () -> Unit
) {
    val backStack = rememberNavBackStack(initialRoute = initialRoute)

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        NavDisplay(
            backStack = backStack,
            onBack = {
                if (backStack.size > 1) {
                    backStack.removeAt(backStack.lastIndex)
                } else {
                    onExitApp()
                }
            },
            transitionSpec = NavTransitions.pushTransition,
            popTransitionSpec = NavTransitions.popTransition
        ) { key ->
            when (key) {
                is Route.MainTab, is Route.LaunchList -> NavEntry(key) {
                    MainTabScreen(
                        onLaunchClick = { launchId ->
                            backStack.add(Route.LaunchDetails(launchId))
                        }
                    )
                }

                is Route.LaunchDetails -> NavEntry(key) {
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