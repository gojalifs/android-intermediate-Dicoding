package com.satria.dicoding.latihan.widgetapp

import java.util.*

internal object NumberGenerator {
    fun generate(max:Int) : Int{
        val random = Random()
        return random.nextInt(max)
    }
}