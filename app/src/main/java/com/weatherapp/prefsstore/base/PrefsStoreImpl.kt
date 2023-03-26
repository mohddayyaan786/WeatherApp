package com.weatherapp.prefsstore.base

import android.content.SharedPreferences
import javax.inject.Inject

class PrefsStoreImpl @Inject constructor(private val preferences: SharedPreferences) : PrefsStore {

    override fun saveString(key: String, value: String) {
        val prefsEditor = preferences.edit()
        prefsEditor.putString(key, value)
        prefsEditor.apply()
    }

    override fun getString(key: String, defValue: String): String {
        return preferences.getString(key, defValue)!!
    }
}