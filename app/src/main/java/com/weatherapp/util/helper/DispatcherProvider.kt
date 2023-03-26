package com.weatherapp.util.helper

import kotlinx.coroutines.Dispatchers

class DispatcherProvider {
    fun getIO() = Dispatchers.IO
    fun getMain() = Dispatchers.Main
    fun getDefault() = Dispatchers.Default
    fun getUnconfined() = Dispatchers.Unconfined
}