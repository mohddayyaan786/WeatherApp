package com.weatherapp.ui.weather

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.weatherapp.ui.BaseViewModel
import com.weatherapp.ui.weather.data.response.WeatherResponse
import com.weatherapp.usecase.WeatherDataUseCaseImpl
import com.weatherapp.util.extension.addSourceDisposable
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val weatherDataUseCase: WeatherDataUseCaseImpl
) : BaseViewModel<WeatherDataResultState>() {
    private var isUserSearchedCity = false
    private val _locationLiveData = MutableLiveData<Location>()
    private val locationLiveData: LiveData<Location> = _locationLiveData
    fun getIsUserSearchedCity() = this.isUserSearchedCity
    fun getLocationLiveData() = this.locationLiveData
    fun setLocationLiveData(location: Location) {
        this._locationLiveData.value = location
    }

    fun getCurrentWeatherInfo(lat: String, lot: String, appId: String) {
        this.isUserSearchedCity = false
        viewModelScope.launch {
            uiState.addSourceDisposable(
                weatherDataUseCase.getCurrentWeatherInfo(lat, lot, appId)
            ) {
                onData {
                    this.let {
                        uiState.value = WeatherDataResultState.Loading(false)
                        uiState.value = WeatherDataResultState.Success(it)
                    }
                }.onMessage {
                    onErrorMessage(this)
                }.onResource {
                    onErrorRes(this)
                }
            }
        }
    }

    fun getCityWeatherInfo(city: String, appId: String) {
        this.isUserSearchedCity = true
        viewModelScope.launch {
            uiState.addSourceDisposable(
                weatherDataUseCase.getCityWeatherInfo(city, appId)
            ) {
                onData {
                    this.let {
                        uiState.value = WeatherDataResultState.Loading(false)
                        uiState.value = WeatherDataResultState.Success(it)
                    }
                }.onMessage {
                    onErrorMessage(this)
                }.onResource {
                    onErrorRes(this)
                }
            }
        }
    }

    private fun onErrorMessage(message: String) {
        uiState.value = WeatherDataResultState.Loading(false)
        uiState.value = WeatherDataResultState.ErrorMessage(message)
    }

    private fun onErrorRes(res: Int) {
        uiState.value = WeatherDataResultState.Loading(false)
        uiState.value = WeatherDataResultState.ErrorRes(res)
    }
}

sealed class WeatherDataResultState {
    data class Loading(val loading: Boolean) : WeatherDataResultState()
    data class Success(val weatherResponse: WeatherResponse) : WeatherDataResultState()
    data class ErrorMessage(val message: String) : WeatherDataResultState()
    data class ErrorRes(val res: Int) : WeatherDataResultState()
}