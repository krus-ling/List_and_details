package com.example.listanddetails.ui.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.listanddetails.domain.model.LaunchItem
import com.example.listanddetails.domain.usecase.GetFavoritesUseCase
import com.example.listanddetails.domain.usecase.ToggleFavoriteUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FavoritesViewModel(
    getFavoritesUseCase: GetFavoritesUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
) : ViewModel() {

    val uiState: StateFlow<FavoritesUiState> = getFavoritesUseCase()
        .map { favorites ->
            FavoritesUiState(items = favorites, isLoading = false)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = FavoritesUiState(isLoading = true)
        )

    fun onToggleFavorite(launch: LaunchItem) {
        viewModelScope.launch {
            toggleFavoriteUseCase(launch)
        }
    }
}
