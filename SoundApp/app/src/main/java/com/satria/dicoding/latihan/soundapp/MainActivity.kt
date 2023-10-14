package com.satria.dicoding.latihan.soundapp

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.SoundPool
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.satria.dicoding.latihan.soundapp.databinding.ActivityMainBinding
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var sp: SoundPool
    private var soundId = 0
    private var isSpLoaded = false
    private var isMediaReady = false

    private var mMediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initSoundPlayer()
        initMediaPlayer()

        binding.btnSoundPool.setOnClickListener {
            if (isSpLoaded) {
                sp.play(soundId, 1f, 1f, 0, 0, 1f)
            }
        }
        binding.btnPlay.setOnClickListener {
            if (!isMediaReady) {
                mMediaPlayer?.prepareAsync()
            } else {
                if (mMediaPlayer?.isPlaying as Boolean) {
                    mMediaPlayer?.pause()
                } else {
                    mMediaPlayer?.start()
                }
            }
        }
        binding.btnStop.setOnClickListener {
            if (mMediaPlayer?.isPlaying as Boolean || isMediaReady) {
                mMediaPlayer?.stop()
                isMediaReady = false
            }
        }
    }

    private fun initMediaPlayer() {
        mMediaPlayer = MediaPlayer()
        val attribute = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()
        mMediaPlayer?.setAudioAttributes(attribute)

        val afd = applicationContext.resources.openRawResourceFd(R.raw.cukurukuk)
        try {
            mMediaPlayer?.setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
        } catch (e: IOException) {
            Log.d("TAG", "initMediaPlayer: $e")
            e.printStackTrace()
        }

        mMediaPlayer?.setOnPreparedListener {
            isMediaReady = true
            mMediaPlayer?.start()
        }
        mMediaPlayer?.setOnErrorListener { _, _, _ -> false }

    }

    private fun initSoundPlayer() {
        sp = SoundPool.Builder().setMaxStreams(10).build()
        sp.setOnLoadCompleteListener { _, _, status ->
            if (status == 0) {
                isSpLoaded = true
            } else {
                Toast.makeText(this, "Failed Load", Toast.LENGTH_SHORT).show()
            }
        }
        soundId = sp.load(this, R.raw.cukurukuk, 1)
    }
}