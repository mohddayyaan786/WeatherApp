package com.weatherapp.usecase

import androidx.lifecycle.MutableLiveData
import com.weatherapp.network.data.ResultData
import com.weatherapp.ui.weather.data.response.WeatherResponse

interface WeatherDataUseCase {
    suspend fun getCurrentWeatherInfo(
        lat: String, lot: String, appId: String
    ): MutableLiveData<ResultData<WeatherResponse>>

    suspend fun getCityWeatherInfo(
        city: String, appId: String
    ): MutableLiveData<ResultData<WeatherResponse>>
}