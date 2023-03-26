package com.weatherapp.usecase

import androidx.lifecycle.MutableLiveData
import com.weatherapp.network.SafeApiRequest
import com.weatherapp.network.data.ResultData
import com.weatherapp.repository.WeatherRepositoryImpl
import com.weatherapp.ui.weather.data.response.WeatherResponse
import com.weatherapp.util.helper.DispatcherProvider
import kotlinx.coroutines.withContext
import javax.inject.Inject

class WeatherDataUseCaseImpl @Inject constructor(
    private val repository: WeatherRepositoryImpl, private val provider: DispatcherProvider
) : WeatherDataUseCase, SafeApiRequest() {

    override suspend fun getCurrentWeatherInfo(
        lat: String, lot: String, appId: String
    ): MutableLiveData<ResultData<WeatherResponse>> {
        val resultLiveData = MutableLiveData<ResultData<WeatherResponse>>()
        withContext(provider.getIO()) {
            apiRequest { repository.getCurrentWeatherInfo(lat, lot, appId) }
        }.onData {
            resultLiveData.value = ResultData.data(this)
        }.onMessageData {
            resultLiveData.value = ResultData.messageData(this)
        }
        return resultLiveData
    }

    override suspend fun getCityWeatherInfo(
        city: String, appId: String
    ): MutableLiveData<ResultData<WeatherResponse>> {
        val resultLiveData = MutableLiveData<ResultData<WeatherResponse>>()

        withContext(provider.getIO()) {
            apiRequest { repository.getCityWeatherInfo(city, appId) }
        }.onData {
            resultLiveData.value = ResultData.data(this)
        }.onMessageData {
            resultLiveData.value = ResultData.messageData(this)
        }
        return resultLiveData
    }
}