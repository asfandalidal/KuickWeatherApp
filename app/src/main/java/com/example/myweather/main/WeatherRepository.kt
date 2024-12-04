package com.example.myweather.main

import com.example.myweather.model.WeatherResponse
import kotlinx.coroutines.flow.Flow


interface WeatherRepository {
    suspend fun getCityData(cityName: String): WeatherResponse
}
