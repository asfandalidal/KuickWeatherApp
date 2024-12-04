package com.example.myweather.network

import com.example.myweather.model.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("weather")
    suspend fun getWeatherForecast(
        @Query("q") cityName: String,
        @Query("appid") apiKey: String
    ): WeatherResponse
}
