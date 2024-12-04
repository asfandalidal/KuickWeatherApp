package com.example.myweather.main

import com.example.myweather.model.WeatherResponse
import com.example.myweather.network.ApiService

class WeatherRepositoryImpl(private val apiService: ApiService) : WeatherRepository {
    override suspend fun getCityData(cityName: String): WeatherResponse {
        return apiService.getWeatherForecast(cityName, "f8a7cf4e052a8672bb9cbc617057dff7")
    }
}
