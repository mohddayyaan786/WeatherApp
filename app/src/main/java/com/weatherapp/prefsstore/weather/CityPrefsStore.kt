package com.weatherapp.prefsstore.weather

interface CityPrefsStore {
    fun getCity(): String
    fun setCity(city: String)
}