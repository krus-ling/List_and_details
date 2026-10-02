package com.example.listanddetails

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.example.listanddetails.ui.details.LaunchDetailScreen
import com.example.listanddetails.ui.list.LaunchListScreen
import com.example.listanddetails.ui.navigation.Route
import com.example.listanddetails.ui.theme.ListAndDetailsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ListAndDetailsTheme {
                val backStack = remember { mutableStateListOf<Route>(Route.LaunchList) }

                BackHandler(enabled = backStack.size > 1) {
                    backStack.removeAt(backStack.lastIndex)
                }

                NavDisplay(
                    backStack = backStack,
                    onBack = { backStack.removeAt(backStack.lastIndex) }
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
                                onBackClick = { backStack.removeAt(backStack.lastIndex) }
                            )
                        }
                    }
                }
            }
        }
    }
}