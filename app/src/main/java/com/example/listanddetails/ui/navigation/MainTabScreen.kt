package com.example.listanddetails.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.example.listanddetails.R
import com.example.listanddetails.ui.agencies.AgencyListScreen
import com.example.listanddetails.ui.astronauts.AstronautListScreen
import com.example.listanddetails.ui.list.LaunchListScreen

private enum class MainTabItem(
    val labelRes: Int,
    val icon: ImageVector
) {
    LAUNCHES(R.string.tab_launches, Icons.Default.RocketLaunch),
    AGENCIES(R.string.tab_agencies, Icons.Default.Business),
    ASTRONAUTS(R.string.tab_astronauts, Icons.Default.Person)
}

@Composable
fun MainTabScreen(
    onLaunchClick: (String) -> Unit
) {
    var selectedTabIndex by rememberSaveable { mutableIntStateOf(0) }

    Scaffold(
        bottomBar = {
            NavigationBar {
                MainTabItem.entries.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        icon = {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = stringResource(item.labelRes)
                            )
                        },
                        label = {
                            Text(stringResource(item.labelRes))
                        }
                    )
                }
            }
        },
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = innerPadding.calculateBottomPadding())
        ) {
            when (selectedTabIndex) {
                0 -> LaunchListScreen(onLaunchClick = onLaunchClick)
                1 -> AgencyListScreen()
                2 -> AstronautListScreen()
            }
        }
    }
}