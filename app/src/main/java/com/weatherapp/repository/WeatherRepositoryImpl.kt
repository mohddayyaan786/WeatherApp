package com.weatherapp.repository

import com.weatherapp.network.service.WeatherApiService
import com.weatherapp.ui.weather.data.response.WeatherResponse
import retrofit2.Response
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(private val service: WeatherApiService) :
    WeatherRepository {

    override suspend fun getCurrentWeatherInfo(
        lat: String, lot: String, appId: String
    ): Response<WeatherResponse> = service.getCurrentWeatherInfo(lat, lot, appId)

    override suspend fun getCityWeatherInfo(
        city: String, appId: String
    ): Response<WeatherResponse> = service.getCityWeatherInfo(city, appId)
}