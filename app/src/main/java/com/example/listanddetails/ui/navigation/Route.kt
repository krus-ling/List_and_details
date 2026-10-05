package com.example.listanddetails.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface Route {
    @Serializable
    data object MainTab : Route

    @Serializable
    data object LaunchList : Route

    @Serializable
    data class LaunchDetails(val id: String) : Route
}