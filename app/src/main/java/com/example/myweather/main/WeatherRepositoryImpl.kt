package com.example.myweather.main

import com.example.myweather.model.WeatherResponse
import com.example.myweather.network.ApiService

class WeatherRepositoryImpl(private val apiService: ApiService) : WeatherRepository {
    override suspend fun getCityData(cityName: String): WeatherResponse {
        return apiService.getWeatherForecast(cityName, api_key)
    }
}
