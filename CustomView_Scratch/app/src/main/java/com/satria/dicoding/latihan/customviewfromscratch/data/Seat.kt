package com.satria.dicoding.latihan.customviewfromscratch.data

data class Seat(
    val id: Int,
    var x: Float? = 0f,
    var y: Float? = 0f,
    var code: String,
    var isBooked: Boolean
)
