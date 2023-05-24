package com.example.myapplication


import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_quran.*

class QuranActivity : AppCompatActivity() {

    private var isPlaying: Boolean = false
    private var playPause: FloatingActionButton? = null
    private var mediaPlayer: MediaPlayer? = null
    private var seekBar: SeekBar? = null
    private var surahList =
        mutableListOf(R.raw.salah_bukhatir_al_fatihah, R.raw.salah_bukhatir_al_kawthar)
    private var surahNameList =
        mutableListOf("Surah al fatiha","Surah al kawthar")

    private var currentSurahIndex: Int = 0
    private var surahName: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quran)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.Qibla_ic -> {
                    clearMediaPlayer()
                    startActivity(Intent(this, QiblaActivity::class.java))
                    true
                }

                R.id.home_ic -> {
                    clearMediaPlayer()

                    startActivity(Intent(this, MainActivity::class.java))
                    true
                }

                R.id.Time_ic -> {
                    clearMediaPlayer()
                    startActivity(Intent(this, TimeActivity::class.java))
                    true
                }

                else -> false
            }
        }

        seekBar = findViewById(R.id.seekbar)
        playPause = findViewById(R.id.play_button)
        surahName = findViewById(R.id.SurahTitle)
        mediaPlayer = MediaPlayer.create(this, surahList[currentSurahIndex])
        surahName?.text = surahNameList[currentSurahIndex].toString()
        controlsound()

        playPause?.setOnClickListener {
            // When the play/pause button is clicked
            if (isPlaying) {
                // If currently playing, pause the audio
                pause()
            } else {
                // If currently paused, play the audio
                play()
            }
            isPlaying = !isPlaying
        }

        next_button.setOnClickListener {
            // When the next button is clicked, play the next surah
            playNext()
        }

        previous_button.setOnClickListener {
            // When the previous button is clicked, play the previous surah
            playPrev()
        }

        repeat_button.setOnClickListener {
            // When the repeat button is clicked
            if (isPlaying) {
                // If currently playing, enable looping and change the repeat button icon
                mediaPlayer?.start()
                mediaPlayer?.isLooping = true
                repeat_button.setImageResource(R.drawable.ic_baseline_repeat_one)
            } else {
                // If currently paused, disable looping and change the repeat button icon
                mediaPlayer?.pause()
                repeat_button.setImageResource(R.drawable.ic_baseline_repeat)
            }
        }

        seekBar?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    // When the seek bar progress is changed by the user, seek to the corresponding position in the audio
                    mediaPlayer?.seekTo(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // Called when the user starts interacting with the seek bar (not implemented in this code)
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // Called when the user stops interacting with the seek bar (not implemented in this code)
            }
        })
    }

    private fun controlsound() {
        // Set the maximum value of the seek bar to the duration of the audio
        mediaPlayer?.setOnPreparedListener {
            seekBar?.max = mediaPlayer!!.duration
            initialiseSeekBar()
        }
    }

    private fun initialiseSeekBar() {
        // Update the seek bar progress based on the current position of the audio
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed(object : Runnable {
            override fun run() {
                try {
                    seekBar?.progress = mediaPlayer!!.currentPosition
                    handler.postDelayed(this, 1000)
                } catch (e: Exception) {
                    seekBar?.progress = 0
                }
            }
        }, 0)
    }

    private fun play() {
        // Start playing the audio
        mediaPlayer?.start()
        initialiseSeekBar()
        Log.d("QuranActivity", "Duration: ${mediaPlayer!!.duration / 1000} seconds")

        val pauseImage = ContextCompat.getDrawable(this, android.R.drawable.ic_media_pause)
        playPause?.setImageDrawable(pauseImage)
    }

    private fun pause() {
        // Pause the audio
        mediaPlayer?.pause()
        Log.d("QuranActivity", "Paused at: ${mediaPlayer!!.currentPosition / 1000} seconds")

        val startImage = ContextCompat.getDrawable(this, android.R.drawable.ic_media_play)
        playPause?.setImageDrawable(startImage)
    }

    private fun playNext() {
        // Play the next surah
        if (currentSurahIndex < surahList.size - 1) {
            currentSurahIndex++
            mediaPlayer?.reset()
            mediaPlayer = MediaPlayer.create(this, surahList[currentSurahIndex])
            mediaPlayer?.start()
            initialiseSeekBar()
            val pauseImage = ContextCompat.getDrawable(this, android.R.drawable.ic_media_pause)
            playPause?.setImageDrawable(pauseImage)
            surahName?.text = surahNameList[currentSurahIndex].toString()

        } else {
            currentSurahIndex = 0
        }
    }

    private fun playPrev() {
        // Play the previous surah
        if (currentSurahIndex > 0) {
            currentSurahIndex--
            mediaPlayer?.reset()
            mediaPlayer = MediaPlayer.create(this, surahList[currentSurahIndex])
            mediaPlayer?.start()
            initialiseSeekBar()
            val pauseImage = ContextCompat.getDrawable(this, android.R.drawable.ic_media_pause)
            playPause?.setImageDrawable(pauseImage)
            surahName?.text = surahNameList[currentSurahIndex].toString()
        } else {
            currentSurahIndex = surahList.size - 1
            mediaPlayer?.reset()
            mediaPlayer = MediaPlayer.create(this, surahList[currentSurahIndex])
            mediaPlayer?.start()
            surahName?.text = surahNameList[currentSurahIndex].toString()
            initialiseSeekBar()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        clearMediaPlayer()
    }

    private fun clearMediaPlayer() {
        // Stop and release the MediaPlayer
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }


}

