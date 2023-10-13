package com.satria.dicoding.latihan.serviceapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.satria.dicoding.latihan.serviceapp.databinding.ActivityMainBinding
import com.satria.dicoding.latihan.serviceapp.services.MyBackgroundService

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnStartBackgroundService.setOnClickListener(this)
        binding.btnStopBackgroundService.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        val serviceIntent = Intent(this, MyBackgroundService::class.java)
        when (v) {
            binding.btnStartBackgroundService -> startService(serviceIntent)
            binding.btnStopBackgroundService -> stopService(serviceIntent)
        }
    }
}