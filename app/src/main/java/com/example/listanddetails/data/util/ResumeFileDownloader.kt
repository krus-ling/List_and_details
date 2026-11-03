package com.example.listanddetails.data.util

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.listanddetails.R
import java.io.File
import androidx.core.net.toUri

class ResumeFileDownloader(private val context: Context) {

    private val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

    fun downloadAndOpenResume(url: String) {
        val trimmedUrl = url.trim()
        if (trimmedUrl.isBlank()) {
            Toast.makeText(context, R.string.resume_url_empty, Toast.LENGTH_SHORT).show()
            return
        }

        val normalizedUrl = if (!trimmedUrl.startsWith("http://", ignoreCase = true) &&
            !trimmedUrl.startsWith("https://", ignoreCase = true)
        ) {
            "https://$trimmedUrl"
        } else {
            trimmedUrl
        }

        try {
            val fileName = "resume_${System.currentTimeMillis()}.pdf"
            val destinationFile = File(
                context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS),
                fileName
            )

            val request = DownloadManager.Request(normalizedUrl.toUri()).apply {
                setTitle(context.getString(R.string.resume_download_title))
                setDescription(context.getString(R.string.resume_download_description))
                setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                setDestinationUri(Uri.fromFile(destinationFile))
                setAllowedOverMetered(true)
                setAllowedOverRoaming(true)
            }

            val downloadId = downloadManager.enqueue(request)
            Toast.makeText(context, R.string.resume_download_started, Toast.LENGTH_SHORT).show()

            registerDownloadReceiver(downloadId, destinationFile)
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, R.string.resume_download_error, Toast.LENGTH_SHORT).show()
        }
    }

    private fun registerDownloadReceiver(downloadId: Long, file: File) {
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(receiverContext: Context?, intent: Intent?) {
                val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1) ?: -1
                if (id == downloadId) {
                    try {
                        context.unregisterReceiver(this)
                    } catch (_: Exception) {}
                    openPdfFile(file)
                }
            }
        }

        val filter = IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        ContextCompat.registerReceiver(
            context,
            receiver,
            filter,
            ContextCompat.RECEIVER_EXPORTED
        )
    }

    fun openPdfFile(file: File) {
        if (!file.exists()) {
            Toast.makeText(context, R.string.resume_file_not_found, Toast.LENGTH_SHORT).show()
            return
        }

        try {
            val authority = "${context.packageName}.fileprovider"
            val contentUri = FileProvider.getUriForFile(context, authority, file)

            val viewIntent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(contentUri, "application/pdf")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }

            context.startActivity(Intent.createChooser(viewIntent, context.getString(R.string.open_resume_chooser)).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            })
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, R.string.no_pdf_app_found, Toast.LENGTH_SHORT).show()
        }
    }
}
