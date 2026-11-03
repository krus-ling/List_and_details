package com.example.listanddetails.ui.profile

import android.net.Uri
import java.io.File

data class EditProfileUiState(
    val name: String = "",
    val position: String = "",
    val photoUri: String = "",
    val resumeUrl: String = "",
    val favoriteLessonTime: String = "",
    val favoriteLessonTimeError: String? = null,
    val isSaved: Boolean = false,
    val pendingCameraUri: Uri? = null,
    val pendingCameraFile: File? = null
) {
    val isSaveEnabled: Boolean
        get() = favoriteLessonTime.isNotBlank() && favoriteLessonTimeError == null
}
