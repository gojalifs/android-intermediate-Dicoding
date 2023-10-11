package com.dicoding.picodiploma.productdetail.data.remote

import android.content.Context
import com.dicoding.picodiploma.productdetail.R
import com.dicoding.picodiploma.productdetail.data.model.ProductModel

class RemoteDataSource(private val context: Context) {
    fun getDetailProduct(): ProductModel {
        return ProductModel(
            name = context.getString(R.string.shoes_name),
            store = context.getString(R.string.seller),
            color = context.getString(R.string.color_variant),
            size = context.getString(R.string.shoes_size),
            desc = context.getString(R.string.description),
            image = R.drawable.shoes,
            date = context.getString(R.string.uploaded_on),
            rating = context.getString(R.string.rating),
            price = context.getString(R.string.price),
            countRating = context.getString(R.string.countRating)
        )
    }
}