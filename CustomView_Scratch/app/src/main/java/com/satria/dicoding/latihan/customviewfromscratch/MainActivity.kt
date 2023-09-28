package com.satria.dicoding.latihan.customviewfromscratch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.satria.dicoding.latihan.customviewfromscratch.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.finishButton.setOnClickListener {
            binding.vSeatsView.seat?.let {
                Toast.makeText(this, "Kursi Anda Nomor ${it.code}", Toast.LENGTH_SHORT).show()
            } ?: run {
                Toast.makeText(this, "Silahkan Pilih Kursi Terlebih Dahulu", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
}