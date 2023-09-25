package com.satria.dicoding.latihan.customview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import com.satria.dicoding.latihan.customview.custom_view.MyButton
import com.satria.dicoding.latihan.customview.custom_view.MyEditText
import com.satria.dicoding.latihan.customview.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setMyButtonEnabled()

        binding.edtMyEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                setMyButtonEnabled()
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })

        binding.btnMybutton.setOnClickListener {
            Toast.makeText(this, binding.edtMyEditText.text, Toast.LENGTH_SHORT).show()
        }
    }

    private fun setMyButtonEnabled() {
        val result = binding.edtMyEditText.text
        binding.btnMybutton.isEnabled = result != null && result.toString().isNotEmpty()
    }
}