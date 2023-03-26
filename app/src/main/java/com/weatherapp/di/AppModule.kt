package com.weatherapp.di

import android.content.Context
import com.weatherapp.util.WeatherApp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun getContext(): Context = WeatherApp.instance
}