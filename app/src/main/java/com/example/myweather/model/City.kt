package com.example.myweather.model

data class City(
    val cityName:String,
    val weather:List<Weather>,
    val main:Main,
    val wind:Wind
)