package com.satria.dicoding.submission.restofinder

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Restaurant(
    val id: String,
    val name: String,
    val description: String,
    val pictureId: String,
    val address: String,
    val city: String,
    val rating: Double,
    val categories: List<Map<String, String>>,
    val menus: Menus
) : Parcelable
