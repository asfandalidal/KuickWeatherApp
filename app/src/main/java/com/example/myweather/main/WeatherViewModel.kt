package com.example.myweather.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myweather.model.City
import com.example.myweather.model.Main
import com.example.myweather.model.Weather
import com.example.myweather.model.Wind
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(private val weatherRepository: WeatherRepository) : ViewModel() {

    private val _weatherResponse: MutableStateFlow<City> = MutableStateFlow(
        City(
            "",
            listOf(Weather("","")),
            Main(),
            Wind()
        )
    )
    val weatherResponse: StateFlow<City> = _weatherResponse

    private val searchChannel = MutableSharedFlow<String>(replay = 1)

    fun setSearchQuery(search: String) {
        searchChannel.tryEmit(search)
    }

    init {
        getCityData()
    }

    private fun getCityData() {
        viewModelScope.launch {
            searchChannel
                .flatMapLatest { search ->
                    weatherRepository.getCityData(search)
                }.catch { e ->
                    Log.e("WeatherViewModel", "Error fetching city data", e)
                    // Optionally update _weatherResponse with an error state
                }.collect { response ->
                    _weatherResponse.value = response
                }
        }
    }
}
