package com.weatherapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel

open class BaseViewModel<T> : ViewModel() {
    fun uiState(): LiveData<T> = uiState
    protected val uiState: MediatorLiveData<T> = MediatorLiveData()
}