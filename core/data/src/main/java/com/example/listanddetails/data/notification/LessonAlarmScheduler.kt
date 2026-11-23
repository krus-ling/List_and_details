package com.example.listanddetails.data.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import java.util.Calendar

class LessonAlarmScheduler(
    private val context: Context
) {
    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    fun scheduleLessonReminder(lessonTime: String, userName: String) {
        val parts = lessonTime.split(":")
        if (parts.size != 2) return

        val hour = parts[0].toIntOrNull() ?: return
        val minute = parts[1].toIntOrNull() ?: return

        val targetCalendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        val now = Calendar.getInstance()
        // Если указанное время на сегодня уже прошло, переносим срабатывание на следующий день
        if (targetCalendar.before(now)) {
            targetCalendar.add(Calendar.DAY_OF_YEAR, 1)
        }

        val intent = Intent(context, LessonReminderReceiver::class.java).apply {
            putExtra(LessonReminderReceiver.EXTRA_USER_NAME, userName)
            putExtra(LessonReminderReceiver.EXTRA_LESSON_TIME, lessonTime)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            REMINDER_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (alarmManager.canScheduleExactAlarms()) {
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        targetCalendar.timeInMillis,
                        pendingIntent
                    )
                } else {
                    alarmManager.setAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        targetCalendar.timeInMillis,
                        pendingIntent
                    )
                }
            } else {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    targetCalendar.timeInMillis,
                    pendingIntent
                )
            }
        } catch (e: SecurityException) {
            alarmManager.set(
                AlarmManager.RTC_WAKEUP,
                targetCalendar.timeInMillis,
                pendingIntent
            )
        }
    }

    fun cancelReminder() {
        val intent = Intent(context, LessonReminderReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            REMINDER_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        )
        if (pendingIntent != null) {
            alarmManager.cancel(pendingIntent)
            pendingIntent.cancel()
        }
    }

    companion object {
        const val REMINDER_REQUEST_CODE = 2001
    }
}
