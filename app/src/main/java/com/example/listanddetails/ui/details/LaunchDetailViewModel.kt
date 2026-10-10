package com.example.listanddetails.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.listanddetails.data.mlkit.MlKitTextTranslator
import com.example.listanddetails.domain.repository.LaunchRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LaunchDetailViewModel(
    private val repository: LaunchRepository,
    private val launchId: String,
    private val translationManager: MlKitTextTranslator = MlKitTextTranslator()
) : ViewModel() {

    private val _uiState = MutableStateFlow(LaunchDetailUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadLaunch()
    }

    fun loadLaunch() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            repository.getLaunchForId(launchId)
                .onSuccess { detail ->
                    _uiState.update { it.copy(isLoading = false, launch = detail) }
                }
                .onFailure { throwable ->
                    _uiState.update { it.copy(isLoading = false, error = throwable.message ?: "Неизвестная ошибка") }
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

    override fun onCleared() {
        super.onCleared()
        translationManager.close()
    }
}