package com.example.myweather.network

import com.example.myweather.model.City
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    @GET("weather/")
    suspend fun getCityWeather(
        @Query("q") q:String,
        @Query("api") api:String
    ): City
}