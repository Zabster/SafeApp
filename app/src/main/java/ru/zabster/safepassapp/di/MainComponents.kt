package ru.zabster.safepassapp.di

import dagger.Component
import ru.zabster.safepassapp.di.annotations.MainScope
import ru.zabster.safepassapp.di.impls.room.RoomHelper
import ru.zabster.safepassapp.di.modules.DataBaseModule

@MainScope
@Component(modules = [DataBaseModule::class])
interface MainComponents {

    /**
     * Database instance
     */
    fun getDataBase(): RoomHelper
}