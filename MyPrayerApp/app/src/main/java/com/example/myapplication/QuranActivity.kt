package com.example.myapplication


import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.SeekBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_quran.*
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream


class QuranActivity : AppCompatActivity() {


    var isPlaying: Boolean = false
    var playPause: FloatingActionButton? = null
    var mMediaPlayer: MediaPlayer? = null
    var mseekBar: SeekBar? = null
    var surahList = mutableListOf(R.raw.salah_bukhatir_al_fatihah)
    var currentsurahIndex : Int = 0
    var surahName:TextView?  = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quran)
        mseekBar = findViewById(R.id.seekbar)
        playPause = findViewById(R.id.play_button)
        controlsound(surahList[0])
        surahName = findViewById(R.id.SurahTitle)

    }


    private fun controlsound(id: Int) {

        playPause?.setOnClickListener {

            if (isPlaying) {
                pause()
            }else{
                mMediaPlayer = MediaPlayer.create(this,id)
                Log.d("QuranActivity", "ID: ${mMediaPlayer!!.audioSessionId}")

                initialiseSeekBar()
                play()
            }
            isPlaying = !isPlaying
     }
        next_button.setOnClickListener{

        }
        previous_button.setOnClickListener{

        }
        mseekBar?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(mseekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser){
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
                    handler.postDelayed(this,1000)
                }catch (e:Exception){
                    mseekBar?.progress = 0
                }
            }
        },0)
    }

    fun play() {
         mMediaPlayer?.start()
        Log.d("QuranActivity", "Duration: ${mMediaPlayer!!.duration/1000} seconds")

        val pauseImage = ContextCompat.getDrawable(this,android.R.drawable.ic_media_pause)
         playPause?.setImageDrawable(pauseImage)


    }

     fun pause() {
        mMediaPlayer?.pause()
         Log.d("QuranActivity", "Paused at: ${mMediaPlayer!!.currentPosition/1000} seconds")

         val startImage = ContextCompat.getDrawable(this,android.R.drawable.ic_media_play)
         playPause?.setImageDrawable(startImage)
    }

    fun playNext(){

        if (currentsurahIndex < (surahList.size - 1)){
            controlsound(currentsurahIndex + 1)
            currentsurahIndex += 1
        }else{
            controlsound(surahList[0])
        }
    }

    fun playPrev(){

        if (currentsurahIndex < (surahList.size + 1)){
            controlsound(currentsurahIndex - 1)
            currentsurahIndex -= 1
        }else{
            controlsound(surahList[0])
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        clearMediaPlayer()
    }

    private fun clearMediaPlayer(){
        mMediaPlayer!!.stop()
        mMediaPlayer!!.release()
    }

}
