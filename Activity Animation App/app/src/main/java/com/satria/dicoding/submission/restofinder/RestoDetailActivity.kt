package com.satria.dicoding.submission.restofinder

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import com.bumptech.glide.Glide
import com.satria.dicoding.submission.restofinder.databinding.ActivityRestoDetailBinding

class RestoDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRestoDetailBinding
    private lateinit var bodyToSend: String

    companion object {
        const val EXTRA_DATA = "restaurant"
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_detail_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_share -> {
                val intent = Intent(Intent.ACTION_SEND)
                // type of the content to be shared
                intent.type = "text/plain"
                val body = bodyToSend

                val subject = "My Favorite Restaurant"
                intent.putExtra(Intent.EXTRA_TEXT, body)
                intent.putExtra(Intent.EXTRA_TITLE, subject)
                intent.putExtra(Intent.EXTRA_SUBJECT, subject)

                startActivity(Intent.createChooser(intent, "Share Using"))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding = ActivityRestoDetailBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val restaurant = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(EXTRA_DATA, Restaurant::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra<Restaurant>(EXTRA_DATA)
        }
        //  set the action bar title text
        supportActionBar?.title = restaurant?.name
        bodyToSend = restaurant?.name ?: "No Data"
        setContentData(restaurant)
    }

    private fun setContentData(restaurant: Restaurant?) {
        Glide.with(this)
            .load("https://restaurant-api.dicoding.dev/images/small/${restaurant?.pictureId}")
            .into(binding.header.imgDetailRestoImage)
        binding.header.tvDetailRestoName.text = restaurant?.name
        binding.header.tvDetailCity.text =
            getString(R.string.address_data, restaurant?.address, restaurant?.city)
        binding.header.tvDtRating.text = "${restaurant?.rating}"
        binding.description.tvDtDescription.text = restaurant?.description
        // TODO: implement additional information => menus, reviews
        addDrinks(restaurant)
        addFoods(restaurant)
    }

    private fun addFoods(restaurant: Restaurant?) {
        val foods = restaurant?.menus?.foods?.map { it["name"] }

        foods?.forEach { food ->
            val tableRow = TableRow(this)
            binding.description.layoutTableFood.addView(tableRow)

            for (i in 1..2) {
                val tv_food = TextView(this)
                tv_food.text = getString(R.string.bullets_menu, food)
                tableRow.addView(tv_food)
                val foodParams = TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT, 1.0f
                )
                foodParams.setMargins(16, 8, 16, 8)
                tv_food.layoutParams = foodParams
                tv_food.textSize = 18f

                val tableParams = TableLayout.LayoutParams()
                tableParams.setMargins(20, 10, 20, 10)
                tableRow.layoutParams = tableParams
            }
        }
    }

    private fun addDrinks(restaurant: Restaurant?) {

        val drinks = restaurant?.menus?.drinks?.map { it["name"] }

        drinks?.forEach { drink ->
            val tableRow = TableRow(this)
            binding.description.layoutTableDrink.addView(tableRow)

            for (i in 1..2) {
                val tv_drink = TextView(this)
                tv_drink.text = getString(R.string.bullets_menu, drink)
                tableRow.addView(tv_drink)
                val textParams = TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT, 1.0f
                )
                textParams.setMargins(16, 8, 16, 8)
                tv_drink.layoutParams = textParams
                tv_drink.textSize = 18f

                val tableParams = TableLayout.LayoutParams()
                tableParams.setMargins(20, 10, 20, 10)
                tableRow.layoutParams = tableParams
            }
        }
    }
}
