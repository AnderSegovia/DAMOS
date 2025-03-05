package com.example.audiopractice

import android.net.Uri
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem

class MainActivity : AppCompatActivity() {
    private var player: ExoPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnPlay = findViewById<Button>(R.id.btnPlay)
        val btnPause = findViewById<Button>(R.id.btnPause)
        val btnStop = findViewById<Button>(R.id.btnStop)

        // Inicializar ExoPlayer
        player = ExoPlayer.Builder(this).build()

        // Cargar audio desde una URL (puedes usar un archivo local también)
        val mediaItem = MediaItem.fromUri(Uri.parse("https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3"))
        player?.setMediaItem(mediaItem)

        btnPlay.setOnClickListener {
            player?.prepare()
            player?.play()
        }

        btnPause.setOnClickListener {
            player?.pause()
        }

        btnStop.setOnClickListener {
            player?.stop()
        }
    }

    override fun onStop() {
        super.onStop()
        player?.release()
        player = null
    }
}