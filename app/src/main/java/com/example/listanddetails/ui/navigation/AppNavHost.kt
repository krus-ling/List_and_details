package com.example.listanddetails.ui.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.example.listanddetails.ui.details.LaunchDetailScreen
import com.example.listanddetails.ui.list.LaunchListScreen

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    initialRoute: Route = Route.LaunchList,
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
//            transitionSpec = NavTransitions.defaultTransitionSpec(),
//            popTransitionSpec = NavTransitions.defaultTransitionSpec()
            transitionSpec = NavTransitions.pushTransition(),
            popTransitionSpec = NavTransitions.popTransition()
        ) { key ->
            when (key) {
                is Route.LaunchList -> NavEntry(key) {
                    LaunchListScreen(
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