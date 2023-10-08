package com.satria.dicoding.submission.restofinder

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Menus(
    val foods: List<Map<String, String>>,
    val drinks: List<Map<String, String>>
) : Parcelable
