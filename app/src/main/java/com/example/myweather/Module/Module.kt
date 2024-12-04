package com.example.myweather.Module

import com.example.myweather.network.ApiService
import com.example.myweather.main.WeatherRepository
import com.example.myweather.main.WeatherRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideBaseUrl(): String {
        return "https://api.openweathermap.org/data/2.5/"
    }

    @Provides
    @Singleton
    fun provideRetrofit(baseUrl: String): Retrofit =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService =
        retrofit.create(ApiService::class.java)

    @Provides
    @Singleton
    fun provideWeatherRepository(apiService: ApiService): WeatherRepository =
        WeatherRepositoryImpl(apiService)
}
