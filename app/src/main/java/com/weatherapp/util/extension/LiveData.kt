package com.weatherapp.util.extension

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.weatherapp.util.helper.SingleBlock

fun <T> MediatorLiveData<*>.addSourceDisposable(
    liveData: LiveData<T>,
    onChange: SingleBlock<T>
): MediatorLiveData<*> {
    addSource(liveData) {
        onChange(it)
        removeSource(liveData)
    }
    return this
}
