package com.example.listanddetails.ui.profile

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.listanddetails.data.notification.LessonAlarmScheduler
import com.example.listanddetails.data.util.ImageStorageManager
import com.example.listanddetails.domain.model.Profile
import com.example.listanddetails.domain.usecase.GetProfileUseCase
import com.example.listanddetails.domain.usecase.SaveProfileUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EditProfileViewModel(
    private val getProfileUseCase: GetProfileUseCase,
    private val saveProfileUseCase: SaveProfileUseCase,
    private val imageStorageManager: ImageStorageManager,
    private val lessonAlarmScheduler: LessonAlarmScheduler
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditProfileUiState())
    val uiState: StateFlow<EditProfileUiState> = _uiState

    init {
        viewModelScope.launch {
            val initialProfile = getProfileUseCase().first()
            val initialTime = initialProfile.favoriteLessonTime
            _uiState.update { state ->
                state.copy(
                    name = initialProfile.name,
                    position = initialProfile.position,
                    photoUri = initialProfile.photoUri,
                    resumeUrl = initialProfile.resumeUrl,
                    favoriteLessonTime = initialTime,
                    favoriteLessonTimeError = validateTime(initialTime)
                )
            }
        }
    }

    fun onNameChange(name: String) {
        _uiState.update { it.copy(name = name) }
    }

    fun onPositionChange(position: String) {
        _uiState.update { it.copy(position = position) }
    }

    fun onResumeUrlChange(url: String) {
        _uiState.update { it.copy(resumeUrl = url) }
    }

    fun onFavoriteLessonTimeChange(time: String) {
        val error = validateTime(time)
        _uiState.update {
            it.copy(
                favoriteLessonTime = time,
                favoriteLessonTimeError = error
            )
        }
    }

    private fun validateTime(time: String): String? {
        if (time.isBlank()) {
            return "Поле обязательно для заполнения"
        }
        if (!TIME_REGEX.matches(time.trim())) {
            return "Неверный формат времени (ожидается ЧЧ:ММ, например 14:30)"
        }
        return null
    }

    fun prepareCameraImageUri(): Uri {
        val (uri, file) = imageStorageManager.createCameraImageUri()
        _uiState.update {
            it.copy(
                pendingCameraUri = uri,
                pendingCameraFile = file
            )
        }
        return uri
    }

    fun onCameraPhotoTaken(success: Boolean) {
        val pendingFile = _uiState.value.pendingCameraFile
        if (success && pendingFile != null && pendingFile.exists()) {
            val savedUri = Uri.fromFile(pendingFile).toString()
            _uiState.update {
                it.copy(
                    photoUri = savedUri,
                    pendingCameraUri = null,
                    pendingCameraFile = null
                )
            }
        } else {
            _uiState.update {
                it.copy(
                    pendingCameraUri = null,
                    pendingCameraFile = null
                )
            }
        }
    }

    fun onGalleryPhotoSelected(uri: Uri?) {
        if (uri == null) return
        val internalUriString = imageStorageManager.copyImageToInternalStorage(uri)
        if (internalUriString != null) {
            _uiState.update { it.copy(photoUri = internalUriString) }
        }
    }

    fun saveProfile() {
        val currentState = _uiState.value
        if (!currentState.isSaveEnabled) return

        viewModelScope.launch {
            saveProfileUseCase(
                Profile(
                    name = currentState.name,
                    position = currentState.position,
                    photoUri = currentState.photoUri,
                    resumeUrl = currentState.resumeUrl,
                    favoriteLessonTime = currentState.favoriteLessonTime.trim()
                )
            )

            // Запланировать напоминание через AlarmManager
            lessonAlarmScheduler.scheduleLessonReminder(
                lessonTime = currentState.favoriteLessonTime.trim(),
                userName = currentState.name.trim()
            )

            _uiState.update { it.copy(isSaved = true) }
        }
    }

    companion object {
        // Регулярное выражение для 24-часового формата времени ЧЧ:ММ (от 00:00 до 23:59)
        private val TIME_REGEX = Regex("^([01]\\d|2[0-3]):[0-5]\\d$")
    }
}
