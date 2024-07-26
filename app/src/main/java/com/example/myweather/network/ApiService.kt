package com.example.myweather.network

import javax.inject.Inject

class ApiService @Inject constructor(private val apiInterface: ApiInterface) {

    suspend fun getCityWeather(cityName:String,apiKey:String) =
        apiInterface.getCityWeather(cityName,apiKey)
}