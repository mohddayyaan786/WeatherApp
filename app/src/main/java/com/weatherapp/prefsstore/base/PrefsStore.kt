package com.weatherapp.prefsstore.base

interface PrefsStore {
    fun saveString(key: String, value: String)
    fun getString(key: String, defValue: String): String
}