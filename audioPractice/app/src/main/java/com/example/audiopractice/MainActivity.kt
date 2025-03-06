package com.example.audiopractice

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageButton
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import java.util.*

class MainActivity : AppCompatActivity() {

    private var player: ExoPlayer? = null
    private lateinit var btnPlayPause: ImageButton
    private lateinit var btnNext: ImageButton
    private lateinit var btnPrev: ImageButton
    private lateinit var btnStop: ImageButton
    private lateinit var seekBar: SeekBar
    private var isPlaying = false
    private var timer: Timer? = null

    private val audioUrls = listOf(
        "https://tonosmovil.net/wp-content/uploads/sonidos-graciosos/Wii_Song.mp3",
        "https://tonosmovil.net/wp-content/uploads/descargar_tonos/Zelda.mp3",
        "https://tonosmovil.net/wp-content/uploads/descargar_tonos2024/Tokyo-Ghoul.mp3",
        "https://youtu.be/FwENr8Mr6SU?si=eTwpWkyLcS_7-ibB",
        "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3"
    )
    private var currentAudioIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnPlayPause = findViewById(R.id.btnPlayPause)
        btnNext = findViewById(R.id.btnNext)
        btnStop = findViewById(R.id.btnStop)

        btnPrev = findViewById(R.id.btnPrev)
        seekBar = findViewById(R.id.seekBar)

        // Inicializa ExoPlayer
        player = ExoPlayer.Builder(this).build()
        cargarYPrepararAudio(currentAudioIndex)

        player?.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(state: Int) {
                if (state == Player.STATE_ENDED) {
                    stopSeekBarUpdater()
                    // Verificar si hay una siguiente canción
                    if (currentAudioIndex < audioUrls.size - 1) {
                        currentAudioIndex++
                        cargarYPrepararAudio(currentAudioIndex)
                    } else {
                        currentAudioIndex = 0
                        cargarYPrepararAudio(currentAudioIndex)
                    }
                }
            }
        })

        btnPlayPause.setOnClickListener {
            if (isPlaying) {
                player?.pause()
                btnPlayPause.setImageResource(R.drawable.ic_play)
                isPlaying = false
            } else {
                player?.play()
                btnPlayPause.setImageResource(R.drawable.ic_pause)
                isPlaying = true
                startSeekBarUpdater()
                startAudioService() // Si tienes el servicio para audio en segundo plano
            }
        }

        // Botón Siguiente: cambia al siguiente audio
        btnNext.setOnClickListener {
            if (currentAudioIndex < audioUrls.size - 1) {
                currentAudioIndex++
                cargarYPrepararAudio(currentAudioIndex)
            }
        }

        // Botón Anterior: vuelve al audio anterior
        btnPrev.setOnClickListener {
            if (currentAudioIndex > 0) {
                currentAudioIndex--
                cargarYPrepararAudio(currentAudioIndex)
            }
        }

        btnStop.setOnClickListener {
            player?.stop() //
            player?.seekTo(0)
            stopAudioService()
            stopSeekBarUpdater()
            seekBar.progress = 0
            player?.prepare()
            btnPlayPause.setImageResource(R.drawable.ic_play)
            isPlaying = false
        }


        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    val duration = player?.duration ?: 0
                    val newPosition = (duration * progress / 100).toLong()
                    player?.seekTo(newPosition)
                }
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        player?.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(state: Int) {
                if (state == Player.STATE_ENDED) {
                    stopSeekBarUpdater()
                    isPlaying = false
                    btnPlayPause.setImageResource(R.drawable.ic_play)
                }
            }
        })
    }


    private fun cargarYPrepararAudio(index: Int) {
        stopSeekBarUpdater()
        player?.stop()
        seekBar.progress = 0
        val mediaItem = MediaItem.fromUri(Uri.parse(audioUrls[index]))
        player?.setMediaItem(mediaItem)
        player?.prepare()
        player?.play()
        btnPlayPause.setImageResource(R.drawable.ic_pause)
        isPlaying = true
        startSeekBarUpdater()
    }

    // Actualizar la barra de progreso cada 500ms
    private fun startSeekBarUpdater() {
        timer = Timer()
        timer?.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                runOnUiThread {
                    player?.let {
                        if (it.duration > 0) {
                            val progress = (it.currentPosition * 100 / it.duration).toInt()
                            seekBar.progress = progress
                        }
                    }
                }
            }
        }, 0, 500)
    }

    private fun stopSeekBarUpdater() {
        timer?.cancel()
        timer = null
    }

    private fun startAudioService() {
        val intent = Intent(this, AudioService::class.java)
        startService(intent)
    }

    private fun stopAudioService() {
        val intent = Intent(this, AudioService::class.java)
        stopService(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        player?.release()
        stopSeekBarUpdater()
        stopAudioService()
    }
}
