package com.example.myweather

import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.myweather.databinding.ActivityMainBinding
import com.example.myweather.main.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlin.math.roundToInt

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val weatherViewModel: WeatherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initListener()

        lifecycleScope.launchWhenStarted {
            weatherViewModel.weatherResponse.collect { response ->

                when (response.weather[0].description) {
                    "clear sky", "mist" -> {
                        Glide.with(this@MainActivity)
                            .load(R.drawable.clouds)
                            .into(binding.image)
                    }
                    "haze", "overcast clouds" -> {
                        Glide.with(this@MainActivity)
                            .load(R.drawable.haze)
                            .into(binding.image)
                    }
                    else -> {
                        Glide.with(this@MainActivity)
                            .load(R.drawable.rain)
                            .into(binding.image)
                    }
                }

                binding.description.text = response.weather[0].description
                binding.cityName.text = response.cityName  // Changed to response.cityName to match your City data class
                binding.degree.text = response.wind.deg.toString()
                binding.speed.text = response.wind.speed.toString()
                binding.temp.text =
                    (((response.main.temp - 273) * 100.0).roundToInt() / 100.0).toString()
                binding.humidity.text = response.main.humidity.toString()

            }
        }
    }

    @ExperimentalCoroutinesApi
    private fun initListener() {
        binding.searchCity.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { weatherViewModel.setSearchQuery(it) }
                Log.d("main", "onQueryTextSubmit: $query")
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Optionally handle text change
                return true
            }
        })
    }
}
