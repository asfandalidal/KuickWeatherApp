package com.example.myweather.main

import com.example.myweather.model.City
import kotlinx.coroutines.flow.Flow


interface WeatherRepository {
    fun getCityData(city: String): Flow<City>
}
