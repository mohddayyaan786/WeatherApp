package com.weatherapp.repository

import com.weatherapp.ui.weather.data.response.WeatherResponse
import retrofit2.Response

interface WeatherRepository {
    suspend fun getCurrentWeatherInfo(
        lat: String, lot: String, appId: String
    ): Response<WeatherResponse>

    suspend fun getCityWeatherInfo(
        city: String, appId: String
    ): Response<WeatherResponse>
}