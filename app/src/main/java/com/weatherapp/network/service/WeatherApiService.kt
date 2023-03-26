package com.weatherapp.network.service

import com.weatherapp.ui.weather.data.response.WeatherResponse

import retrofit2.Response
import retrofit2.http.*
import java.util.*

interface WeatherApiService {

    @GET("/data/2.5/weather")
    suspend fun getCurrentWeatherInfo(
        @Query("lat") page: String, @Query("lon") perPage: String, @Query("appid") appid: String
    ): Response<WeatherResponse>

    @GET("/data/2.5/weather")
    suspend fun getCityWeatherInfo(
        @Query("q") q: String, @Query("appid") appid: String
    ): Response<WeatherResponse>
}