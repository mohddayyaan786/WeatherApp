package com.weatherapp.di

import android.content.Context
import android.content.SharedPreferences
import com.weatherapp.prefsstore.base.PrefsStoreImpl
import com.weatherapp.util.SHARED_PREFS
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class PrefsModule {

    @Provides
    @Singleton
    fun provideSharedPreference(@ApplicationContext context: Context): SharedPreferences =
        context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)

    @Provides
    @Singleton
    fun providePrefsStore(preferences: SharedPreferences): PrefsStoreImpl =
        PrefsStoreImpl(preferences)


}