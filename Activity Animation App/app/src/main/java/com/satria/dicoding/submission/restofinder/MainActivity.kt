package com.satria.dicoding.submission.restofinder

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

class MainActivity : AppCompatActivity() {
    private lateinit var rvRestaurants: RecyclerView
    private val restoList = ArrayList<Restaurant>()

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val intent = Intent(this@MainActivity, AboutActivity::class.java)
        startActivity(intent)
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContentView(R.layout.activity_main)

        rvRestaurants = findViewById(R.id.rv_restos)
        rvRestaurants.setHasFixedSize(true)

        restoList.addAll(getRestoList())
        showRecycleList()
    }

    private fun showRecycleList() {
        if (applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            rvRestaurants.layoutManager = LinearLayoutManager(this)
        } else {
            rvRestaurants.layoutManager = GridLayoutManager(this, 2)
        }
        rvRestaurants.adapter = ListRestaurantAdapter(restoList)
    }

    private fun getRestoList(): List<Restaurant> {

        return try {
            val inputStream = resources.openRawResource(R.raw.restaurant_list)
            val jsonString = readInputStream(inputStream)
            val data = Gson().fromJson(jsonString, Array<Restaurant>::class.java).toList()
            data
        } catch (e: Exception) {
            Toast.makeText(this, "Failed To Get Data", Toast.LENGTH_SHORT).show()
            emptyList()
        }
    }

    private fun readInputStream(inputStream: InputStream): String {
        val reader = BufferedReader(InputStreamReader(inputStream))
        val stringBuilder = StringBuilder()
        var line: String?
        while (reader.readLine().also { line = it } != null) {
            stringBuilder.append(line)
        }
        reader.close()
        return stringBuilder.toString()
    }
}