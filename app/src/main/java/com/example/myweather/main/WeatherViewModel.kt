package com.example.myweather.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myweather.model.WeatherResponse
import com.example.myweather.network.ApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository
) : ViewModel() {
    private val _weatherResponse = MutableStateFlow<WeatherResponse?>(null)
    val weatherResponse: StateFlow<WeatherResponse?> = _weatherResponse

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun setSearchQuery(query: String, isInitialFetch: Boolean = false) {
        viewModelScope.launch {
            try {
                val response = weatherRepository.getCityData(query)
                _weatherResponse.value = response
            } catch (e: Exception) {
                if (!isInitialFetch) {
                    _errorMessage.value = "City not found. Please try again."
                }
            }
        }
    }
}

