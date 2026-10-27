package com.example.listanddetails.data.util

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream

class ImageStorageManager(private val context: Context) {

    fun createCameraImageUri(): Pair<Uri, File> {
        val imagesDir = File(context.filesDir, "images").apply {
            if (!exists()) mkdirs()
        }
        val file = File(imagesDir, "avatar_${System.currentTimeMillis()}.jpg")
        val authority = "${context.packageName}.fileprovider"
        val uri = FileProvider.getUriForFile(context, authority, file)
        return Pair(uri, file)
    }

    fun copyImageToInternalStorage(sourceUri: Uri): String? {
        return try {
            val imagesDir = File(context.filesDir, "images").apply {
                if (!exists()) mkdirs()
            }
            val destinationFile = File(imagesDir, "avatar_${System.currentTimeMillis()}.jpg")
            context.contentResolver.openInputStream(sourceUri)?.use { input ->
                FileOutputStream(destinationFile).use { output ->
                    input.copyTo(output)
                }
            }
            Uri.fromFile(destinationFile).toString()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
