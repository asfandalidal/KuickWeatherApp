package com.example.myweather.main

import com.example.myweather.model.City
import com.example.myweather.network.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(private val apiService: ApiService):
    WeatherRepository {

    override fun getCityData(city: String): Flow<City> = flow {
        val response = apiService.getCityWeather(city,"f8a7cf4e052a8672bb9cbc617057dff7")
        emit(response)
    }.flowOn(Dispatchers.IO)
        .conflate()

    }

