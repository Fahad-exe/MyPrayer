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
import androidx.core.content.ContextCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_quran.*

class QuranActivity : AppCompatActivity() {


    var isPlaying: Boolean = false
    var playPause: FloatingActionButton? = null
    var mMediaPlayer: MediaPlayer? = null
    var mseekBar: SeekBar? = null
    var surahList = mutableListOf(R.raw.salah_bukhatir_al_fatihah, R.raw.salah_bukhatir_al_kawthar)
    var currentsurahIndex: Int = 0
    var surahName: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quran)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        bottomNavigationView.setOnItemSelectedListener() { menuItem ->
            when (menuItem.itemId) {
                R.id.Qibla_ic -> {
                    // Handle the home action
                    startActivity(Intent(this, QiblaActivity::class.java))
                    true
                }
                R.id.home_ic -> {
                    // Handle the profile action
                    startActivity(Intent(this, MainActivity::class.java))
                    true
                }
                R.id.Time_ic -> {
                    // Handle the settings action
                    startActivity(Intent(this, TimeActivity::class.java))
                    true
                }
                else -> false
            }
        }


        mseekBar = findViewById(R.id.seekbar)
        playPause = findViewById(R.id.play_button)
        controlsound(surahList[currentsurahIndex])
        surahName = findViewById(R.id.SurahTitle)
        mMediaPlayer = MediaPlayer.create(this, surahList[currentsurahIndex])


    }


    private fun controlsound(id: Int) {

        playPause?.setOnClickListener {

            if (isPlaying) {
                pause()

            } else {
                MediaPlayer.create(this, surahList[currentsurahIndex])
                MediaPlayer.MEDIA_INFO_METADATA_UPDATE
                Log.d("QuranActivity", "ID: ${mMediaPlayer!!.audioSessionId}")
                play()


            }
            isPlaying = !isPlaying
        }
        next_button.setOnClickListener {
            if (!isPlaying) {

                mMediaPlayer?.pause()
                mMediaPlayer?.release()

            }else{

                    playNext()

            }

        }
        previous_button.setOnClickListener {
            if (isPlaying) {
                mMediaPlayer?.release()
                playPrev()
            }
            playPrev()
        }
        mseekBar?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(mseekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    mMediaPlayer?.seekTo(progress)
                }
            }

            override fun onStartTrackingTouch(mseekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(mseekBar: SeekBar?) {
            }
        })

    }

    private fun initialiseSeekBar() {
        mseekBar?.max = mMediaPlayer!!.duration

        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed(object : Runnable {
            override fun run() {
                try {
                    mseekBar?.progress = mMediaPlayer!!.currentPosition
                    handler.postDelayed(this, 1000)
                } catch (e: Exception) {
                    mseekBar?.progress = 0
                }
            }
        }, 0)
    }

    fun play() {
        mMediaPlayer?.start()
        initialiseSeekBar()
        Log.d("QuranActivity", "Duration: ${mMediaPlayer!!.duration / 1000} seconds")

        val pauseImage = ContextCompat.getDrawable(this, android.R.drawable.ic_media_pause)
        playPause?.setImageDrawable(pauseImage)


    }

    fun pause() {

        mMediaPlayer?.pause()
        Log.d("QuranActivity", "Paused at: ${mMediaPlayer!!.currentPosition / 1000} seconds")

        val startImage = ContextCompat.getDrawable(this, android.R.drawable.ic_media_play)
        playPause?.setImageDrawable(startImage)
    }

    fun playNext() {

        if (currentsurahIndex < (surahList.size - 1)) {
            currentsurahIndex += 1
            mMediaPlayer
            MediaPlayer.create(this, surahList[currentsurahIndex])
            mMediaPlayer?.start()

        } else {
            currentsurahIndex = 0
        }
    }

    fun playPrev() {

        if (currentsurahIndex < (surahList.size + 1)) {
            controlsound(currentsurahIndex - 1)
            currentsurahIndex -= 1
            MediaPlayer.create(this, surahList[currentsurahIndex])
            mMediaPlayer?.start()

        } else {
            currentsurahIndex = 0
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        clearMediaPlayer()
    }

    private fun clearMediaPlayer() {
        mMediaPlayer!!.stop()
        mMediaPlayer!!.release()
    }


}

