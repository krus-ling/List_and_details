package com.example.listanddetails.data.util

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

class ImageStorageManager(private val context: Context) {

    fun createCameraImageUri(): Pair<Uri, File> {
        val imagesDir = File(context.filesDir, "profile_images").apply { mkdirs() }
        val imageFile = File(imagesDir, "avatar_${UUID.randomUUID()}.jpg")
        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            imageFile
        )
        return uri to imageFile
    }

    fun copyImageToInternalStorage(sourceUri: Uri): String? {
        return runCatching {
            val imagesDir = File(context.filesDir, "profile_images").apply { mkdirs() }
            val destinationFile = File(imagesDir, "avatar_${UUID.randomUUID()}.jpg")

            context.contentResolver.openInputStream(sourceUri)?.use { input ->
                FileOutputStream(destinationFile).use { output ->
                    input.copyTo(output)
                }
            }
            Uri.fromFile(destinationFile).toString()
        }.getOrNull()
    }
}
