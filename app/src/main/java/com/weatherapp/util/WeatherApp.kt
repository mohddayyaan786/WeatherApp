package com.weatherapp.util

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class WeatherApp : Application() {
    companion object {
        lateinit var instance: WeatherApp
    }

    override fun onCreate() {
        instance = this
        super.onCreate()
    }
}