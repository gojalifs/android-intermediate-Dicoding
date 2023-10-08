package com.satria.dicoding.submission.restofinder

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.satria.dicoding.submission.restofinder.databinding.ItemRestoCardBinding
import androidx.core.util.Pair

class ListRestaurantAdapter(private val listResto: ArrayList<Restaurant>) :
    RecyclerView.Adapter<ListRestaurantAdapter.ListViewHolder>() {

    inner class ListViewHolder(var binding: ItemRestoCardBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding =
            ItemRestoCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun getItemCount(): Int = listResto.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val name = listResto[position].name
        val city = listResto[position].city
        val rating = listResto[position].rating
        val pictId = listResto[position].pictureId

        Glide.with(holder.itemView.context)
            .load("https://restaurant-api.dicoding.dev/images/small/$pictId")
            .into(holder.binding.imgRestoImage)
        holder.binding.tvRestoName.text = name
        holder.binding.tvCity.text = city
        holder.binding.tvRating.text = "$rating"

        holder.itemView.setOnClickListener {
            val optionsCompat =
                ActivityOptionsCompat.makeSceneTransitionAnimation(
                    holder.itemView.context as Activity,
                    Pair(holder.binding.imgRestoImage, "profile"),
                    Pair(holder.binding.tvRestoName, "name"),
                )

            val intent = Intent(holder.itemView.context, RestoDetailActivity::class.java)
            intent.putExtra(RestoDetailActivity.EXTRA_DATA, listResto[position])
            holder.itemView.context.startActivity(intent, optionsCompat.toBundle())
        }
    }
}