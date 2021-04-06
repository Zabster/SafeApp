package ru.zabster.safepassapp.di.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

import ru.zabster.safepassapp.di.impls.rescontents.ResourceManager
import ru.zabster.safepassapp.di.impls.rescontents.ResourceManagerImpl

@Module
class ResourceModule(private val context: Context) {

    @Provides
    @Singleton
    fun provideResourceManager(): ResourceManager = ResourceManagerImpl(context)
}