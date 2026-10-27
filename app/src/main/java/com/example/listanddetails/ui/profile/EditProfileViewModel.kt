package com.example.listanddetails.ui.profile

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    private val imageStorageManager: ImageStorageManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditProfileUiState())
    val uiState: StateFlow<EditProfileUiState> = _uiState

    init {
        viewModelScope.launch {
            val initialProfile = getProfileUseCase().first()
            _uiState.update { state ->
                state.copy(
                    name = initialProfile.name,
                    position = initialProfile.position,
                    photoUri = initialProfile.photoUri,
                    resumeUrl = initialProfile.resumeUrl
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
        viewModelScope.launch {
            saveProfileUseCase(
                Profile(
                    name = currentState.name,
                    position = currentState.position,
                    photoUri = currentState.photoUri,
                    resumeUrl = currentState.resumeUrl
                )
            )
            _uiState.update { it.copy(isSaved = true) }
        }
    }
}
