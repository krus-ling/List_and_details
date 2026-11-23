package com.example.listanddetails.data.notification

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.listanddetails.MainActivity
import com.example.listanddetails.R

class LessonReminderReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val userName = intent.getStringExtra(EXTRA_USER_NAME).orEmpty()
        val lessonTime = intent.getStringExtra(EXTRA_LESSON_TIME).orEmpty()

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
            ?: return

        // Создаем NotificationChannel для Android 8.0+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                context.getString(R.string.lesson_notification_channel_name),
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = context.getString(R.string.lesson_notification_channel_desc)
                enableVibration(true)
            }
            notificationManager.createNotificationChannel(channel)
        }

        // По нажатию на push-уведомление открывается наше приложение
        val openAppIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            NOTIFICATION_REQUEST_CODE,
            openAppIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notificationTitle = context.getString(R.string.lesson_notification_title)
        val notificationText = if (userName.isNotBlank()) {
            context.getString(R.string.lesson_notification_text_with_name, userName, lessonTime)
        } else {
            context.getString(R.string.lesson_notification_text, lessonTime)
        }

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification_alarm)
            .setContentTitle(notificationTitle)
            .setContentText(notificationText)
            .setStyle(NotificationCompat.BigTextStyle().bigText(notificationText))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        // Проверяем runtime-разрешение POST_NOTIFICATIONS для Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val hasPermission = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED

            if (!hasPermission) {
                return
            }
        }

        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    companion object {
        const val CHANNEL_ID = "favorite_lesson_reminder_channel"
        const val NOTIFICATION_ID = 1001
        const val NOTIFICATION_REQUEST_CODE = 1002
        const val EXTRA_USER_NAME = "extra_user_name"
        const val EXTRA_LESSON_TIME = "extra_lesson_time"
    }
}
