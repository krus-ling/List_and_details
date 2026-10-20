package com.example.listanddetails.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.listanddetails.domain.repository.TextTranslator
import com.example.listanddetails.domain.usecase.GetLaunchDetailUseCase
import com.example.listanddetails.domain.usecase.ObserveIsFavoriteUseCase
import com.example.listanddetails.domain.usecase.ToggleFavoriteUseCase
import com.example.listanddetails.ui.components.handleAppError
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LaunchDetailViewModel(
    private val getLaunchDetailUseCase: GetLaunchDetailUseCase,
    private val launchId: String,
    private val translationManager: TextTranslator,
    private val observeIsFavoriteUseCase: ObserveIsFavoriteUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LaunchDetailUiState())
    val uiState = _uiState.asStateFlow()

    val isFavorite: StateFlow<Boolean> = observeIsFavoriteUseCase(launchId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false
        )

    init {
        loadLaunch()
    }

    fun onToggleFavorite() {
        val detail = _uiState.value.launch ?: return
        viewModelScope.launch {
            toggleFavoriteUseCase(detail)
        }
    }

    fun loadLaunch() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            getLaunchDetailUseCase(launchId)
                .onSuccess { detail ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            launch = detail
                        )
                    }
                }
                .onFailure { throwable ->
                    val errorResult = throwable.handleAppError()
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = errorResult.errorMessage,
                        )
                    }
                }
        }
    }

    fun toggleTranslation() {
        val currentLaunch = _uiState.value.launch ?: return

        // Если уже переведено — мгновенно возвращаем оригинал
        if (_uiState.value.isTranslated) {
            _uiState.update { it.copy(isTranslated = false) }
            return
        }

        // Если перевод уже кэширован в состоянии — просто включаем его
        if (_uiState.value.translatedDescription != null || _uiState.value.translatedFailReason != null) {
            _uiState.update { it.copy(isTranslated = true) }
            return
        }

        // Первый запрос на перевод: переводим описание миссии и причину сбоя (если есть)
        viewModelScope.launch {
            _uiState.update { it.copy(isTranslating = true) }

            val desc = currentLaunch.missionDescription
            val failReason = currentLaunch.failReason
            val missionType = currentLaunch.missionType
            val orbit = currentLaunch.orbit

            val (transDesc, transFail, transType, transOrbit) = coroutineScope {
                val d = async { if (!desc.isNullOrBlank()) translationManager.translate(desc).getOrNull() else null }
                val f = async { if (!failReason.isNullOrBlank()) translationManager.translate(failReason).getOrNull() else null }
                val m = async { if (!missionType.isNullOrBlank()) translationManager.translate(missionType).getOrNull() else null }
                val o = async { if (!orbit.isNullOrBlank()) translationManager.translate(orbit).getOrNull() else null }
                listOf(d.await(), f.await(), m.await(), o.await())
            }

            _uiState.update {
                it.copy(
                    isTranslating = false,
                    isTranslated = true,
                    translatedDescription = transDesc ?: desc,
                    translatedFailReason = transFail ?: failReason,
                    translatedMissionType = transType ?: missionType,
                    translatedOrbit = transOrbit ?: orbit
                )
            }
        }
    }
}