package com.weatherapp.prefsstore.weather

import com.weatherapp.prefsstore.base.PrefsStoreImpl
import com.weatherapp.util.CITY
import javax.inject.Inject

class CityPrefsStoreImpl @Inject constructor(private val prefsStore: PrefsStoreImpl) :
    CityPrefsStore {
    override fun getCity(): String = prefsStore.getString(CITY, "")
    override fun setCity(city: String) = prefsStore.saveString(CITY, city)
}
