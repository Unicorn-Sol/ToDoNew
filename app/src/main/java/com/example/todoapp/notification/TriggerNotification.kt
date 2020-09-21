package com.example.todoapp.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.todoapp.R
import java.util.*

class TriggerNotification(context: Context, title: String, body: String) {

    init {
        sendNotification(context, title, body)
    }

    private fun createNotificationChannel(context: Context, name: String, description: String): String {
        val chanelId = UUID.randomUUID().toString()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(chanelId, name, importance)
            channel.enableLights(true)
            channel.enableVibration(true)
            channel.description = description
            channel.lightColor = Color.BLUE
            channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC

            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(channel)
        }

        return chanelId
    }

    private fun sendNotification(context: Context, title: String, body: String) {

        val notificationManager = NotificationManagerCompat.from(context)
        val mBuilder = NotificationCompat.Builder(context, createNotificationChannel(context, title, body))
        val notificationId = (System.currentTimeMillis() and 0xfffffff).toInt()

        mBuilder.setDefaults(Notification.DEFAULT_ALL)
            .setTicker("Hearty365")
            .setContentTitle(title)
            .setContentText(body)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setSmallIcon(R.drawable.ic_baseline_timer_24)
            .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.drawable.unicorn))
            .setContentInfo("Content Info")
            .setAutoCancel(false)

        notificationManager.notify(notificationId, mBuilder.build())
    }


}