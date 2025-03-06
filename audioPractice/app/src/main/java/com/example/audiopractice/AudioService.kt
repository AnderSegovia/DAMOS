package com.example.audiopractice

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem

class AudioService : Service() {

    private var player: ExoPlayer? = null
    private val CHANNEL_ID = "AUDIO_SERVICE_CHANNEL"

    override fun onCreate() {
        super.onCreate()
        player = ExoPlayer.Builder(this).build()
        val mediaItem = MediaItem.fromUri("https://tonosmovil.net/wp-content/uploads/descargar_tonos/Zelda.mp3")
        player?.setMediaItem(mediaItem)
        player?.prepare()
        player?.play()
        showNotification()
    }

    private fun showNotification() {
        val notificationManager = getSystemService(NotificationManager::class.java)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Audio Service",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notification: Notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Reproduciendo audio")
            .setContentText("¡Tu audio está sonando en segundo plano!")
            .setSmallIcon(android.R.drawable.ic_media_play)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        startForeground(1, notification)
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        player?.release()
    }
}
