package com.sohail.attendancetracker.notification

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.sohail.attendancetracker.MainActivity
import com.sohail.attendancetracker.R

/**
 * Broadcast receiver for attendance notifications
 */
class AttendanceNotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val percentage = intent.getFloatExtra("percentage", 0f)
        AttendanceNotificationHelper.showLowAttendanceNotification(context, percentage)
    }
}

/**
 * Helper class for managing attendance notifications
 */
object AttendanceNotificationHelper {
    
    private const val CHANNEL_ID = "attendance_alerts"
    private const val NOTIFICATION_ID = 1001
    
    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = context.getString(R.string.notification_channel_name)
            val descriptionText = context.getString(R.string.notification_channel_description)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
    
    fun showLowAttendanceNotification(context: Context, percentage: Float) {
        // Check notification permission for Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
        }
        
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        
        val title = context.getString(R.string.low_attendance_title)
        val text = context.getString(R.string.low_attendance_body, percentage)
        
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_alert)
            .setContentTitle(title)
            .setContentText(text)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()
        
        with(NotificationManagerCompat.from(context)) {
            notify(NOTIFICATION_ID, notification)
        }
    }
    
    /**
     * Check attendance and show notification if below threshold
     */
    suspend fun checkAndNotify(
        context: Context,
        percentage: Float,
        notificationsEnabled: Boolean,
        lastNotificationTime: Long
    ) {
        if (!notificationsEnabled) return
        
        // Show notification if attendance is below 72%
        if (percentage < 72f) {
            // Don't spam notifications - wait at least 24 hours between notifications
            val currentTime = System.currentTimeMillis()
            val timeSinceLastNotification = currentTime - lastNotificationTime
            val oneDayInMillis = 24 * 60 * 60 * 1000L
            
            if (timeSinceLastNotification > oneDayInMillis) {
                showLowAttendanceNotification(context, percentage)
            }
        }
    }
}
