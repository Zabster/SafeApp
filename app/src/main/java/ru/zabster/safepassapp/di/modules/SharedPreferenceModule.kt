package ru.zabster.safepassapp.di.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

import ru.zabster.safepassapp.di.impls.shared.SharedPreferenceHelper
import ru.zabster.safepassapp.di.impls.shared.SharedPreferenceImpl

@Module
class SharedPreferenceModule(private val context: Context) {

    @Provides
    @Singleton
    fun provideSharedPreference(): SharedPreferenceHelper = SharedPreferenceImpl(context)
}