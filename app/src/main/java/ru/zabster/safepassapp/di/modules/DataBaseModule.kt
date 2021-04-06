package ru.zabster.safepassapp.di.modules

import android.content.Context
import androidx.room.migration.Migration
import dagger.Module
import dagger.Provides
import ru.zabster.safepassapp.di.annotations.MainScope
import javax.inject.Singleton

import ru.zabster.safepassapp.di.impls.room.RoomHelper
import ru.zabster.safepassapp.di.impls.room.RoomImpl

@Module
class DataBaseModule(
    private val context: Context,
    private val migrations: Array<Migration>,
    private val password: String
) {

    @Provides
    @MainScope
    fun provideDataBase(): RoomHelper = RoomImpl(context, password, migrations)
}