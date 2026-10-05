package com.example.listanddetails.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.toMutableStateList
import kotlinx.serialization.json.Json

@Composable
fun rememberNavBackStack(initialRoute: Route): SnapshotStateList<Route> {
    return rememberSaveable(
        saver = listSaver(
            save = { list ->
                list.map { Json.encodeToString(Route.serializer(), it) }
            },
            restore = { saved ->
                saved.map { Json.decodeFromString(Route.serializer(), it) }.toMutableStateList()
            }
        )
    ) {
        SnapshotStateList<Route>().apply { add(initialRoute) }
    }
}