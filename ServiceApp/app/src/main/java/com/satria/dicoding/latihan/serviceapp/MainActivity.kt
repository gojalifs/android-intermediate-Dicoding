package com.satria.dicoding.latihan.serviceapp

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.satria.dicoding.latihan.serviceapp.databinding.ActivityMainBinding
import com.satria.dicoding.latihan.serviceapp.services.MyBackgroundService
import com.satria.dicoding.latihan.serviceapp.services.MyForegroundService
import android.Manifest
import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder
import com.satria.dicoding.latihan.serviceapp.services.MyBoundService

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var boundStatus = false
    private lateinit var boundService: MyBoundService

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val myBinder = service as MyBoundService.MyBinder
            boundService = myBinder.getService
            boundStatus = true
            getNumberFromService()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            boundStatus = false
        }

    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Notifications permission granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Notifications permission rejected", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (Build.VERSION.SDK_INT >= 33) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

        val serviceIntent = Intent(this, MyBackgroundService::class.java)
        val foregroundServiceIntent = Intent(this, MyForegroundService::class.java)
        val boundServiceIntent = Intent(this, MyBoundService::class.java)

        binding.btnStartBackgroundService.setOnClickListener {

            startService(serviceIntent)
        }
        binding.btnStopBackgroundService.setOnClickListener { stopService(serviceIntent) }
        binding.btnStartForegroundService.setOnClickListener {

            if (Build.VERSION.SDK_INT >= 26) {
                startForegroundService(foregroundServiceIntent)
            } else {
                startService(foregroundServiceIntent)
            }
        }
        binding.btnStopForegroundService.setOnClickListener {
            stopService(foregroundServiceIntent)
        }
        binding.btnStartBoundService.setOnClickListener {
            bindService(boundServiceIntent, connection, BIND_AUTO_CREATE)
        }
        binding.btnStopBoundService.setOnClickListener { unbindService(connection) }
    }

    private fun getNumberFromService() {
        boundService.numberLiveData.observe(this) {
            binding.tvBoundServiceNumber.text = it.toString()
        }
    }

    override fun onStop() {
        super.onStop()
        if (boundStatus) {
            unbindService(connection)
            boundStatus = false
        }
    }

}