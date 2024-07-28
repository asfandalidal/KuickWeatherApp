package com.example.myweather

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.myweather.databinding.ActivityMainBinding
import com.example.myweather.main.WeatherViewModel
import com.example.myweather.model.WeatherResponse
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
        fetchInitialWeatherData()

        lifecycleScope.launchWhenStarted {
            weatherViewModel.weatherResponse.collect { response ->
                response?.let {
                    Log.d("MainActivity", "Weather data received: $response")
                    updateUI(response)
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            weatherViewModel.errorMessage.collect { error ->
                error?.let {
                    Log.e("MainActivity", it)
                    showCityNotFoundDialog() // Show dialog on error
                }
            }
        }
    }

    private fun fetchInitialWeatherData() {
        weatherViewModel.setSearchQuery("Karachi",true)
    }

    private fun updateUI(response: WeatherResponse) {
        binding.description.text = response.weather[0].description
        binding.cityName.text = response.name
        binding.degree.text = "${response.wind.deg}°"
        binding.speed.text = "${response.wind.speed} m/s"
        binding.temp.text = "${((response.main.temp - 273.15) * 100.0).roundToInt() / 100.0} °C"
        binding.humidity.text = "${response.main.humidity} %"

        val weatherDescription = response.weather[0].description
        val imageResource = when (weatherDescription) {
            "clear sky", "few clouds" -> R.drawable.cloud
            "broken clouds", "overcast clouds", "haze" -> R.drawable.fog
            else -> R.drawable.rainy
        }

        Glide.with(this).load(imageResource).into(binding.image)
    }

    @ExperimentalCoroutinesApi
    private fun initListener() {
        binding.searchCity.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    weatherViewModel.setSearchQuery(it)
                    Log.d("MainActivity", "Search query submitted: $query")
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Optionally handle text change
                return true
            }
        })
    }

    private fun showCityNotFoundDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_city_not_found, null)
        val builder = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(true)
        val dialog = builder.create()

        val dialogImage = dialogView.findViewById<ImageView>(R.id.dialog_image)
        val dialogText = dialogView.findViewById<TextView>(R.id.dialog_text)
        val dialogButton = dialogView.findViewById<Button>(R.id.dialog_button)

        dialogImage.setImageResource(R.drawable.umbrella)
        dialogText.text = "City Not Found"
        dialogButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }
}
